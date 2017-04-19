(ns aoc.day11
  (:require [ysera.test :refer [is is-not is=]]))

(def initial-state-example
  {:lig 3
   :hyg 2
   :hym 1 :lim 1 :ele 1})

(def initial-state-puzzle-a
  {:com 3 :cum 3 :plm 3 :rum 3
   :cog 2 :cug 2 :plg 2 :rug 2
   :prg 1 :prm 1 :ele 1})

; Maps micro chips to generators
(def chip-to-generator
  (hash-map :com :cog :cum :cug :hym :hyg :lim :lig :plm :plg :prm :prg :rum :rug))

(def all-chips
  (apply hash-set (keys chip-to-generator)))

(def all-generators
  (apply hash-set (vals chip-to-generator)))

(defn on-floor
  {:doc  "Return items on floor."
   :test #(do
            (is= (on-floor 1 {:a 3 :b 3 :c 4}) [])
            (is= (on-floor 3 {:a 3 :b 3 :c 4}) [:a :b]))}
  [floor state]
  (map key (filter #(= (val %) floor) state)))

(defn move-to
  {:doc  "Move item to floor."
   :test #(do
            (is= (move-to :a 1 {:a 3 :b 4}) {:a 1 :b 4})
            (is= (move-to :a 1 {:a 3 :b 3}) {:a 1 :b 3}))}
  [item floor state]
  (assoc state item floor))

(defn make-move
  {:doc  "Make the given move and return the updated state."
   :test #(do
            (is= (make-move {:floor 2 :items [:cog :com :ele]} {:cog 1 :com 1 :ele 1})
                 {:cog 2 :com 2 :ele 2})
            (is= (make-move {:floor 2 :items [:cog :ele]} {:cog 1 :com 1 :ele 1})
                 {:cog 2 :com 1 :ele 2}))}
  [move state]
  (reduce (fn [acc item] (move-to item (:floor move) acc))
          state
          (:items move)))

(defn generator
  {:doc  "Return the generator matching chip."
   :test #(do
            (is= (generator :lim) :lig))}
  [chip]
  (get chip-to-generator chip))

(defn generators
  {:doc  "Return a sequence of generators matching the given sequence of chips."
   :test #(do
            (is= (generators [:lim :hym]) [:lig :hyg]))}
  [chips]
  (map generator chips))

(defn chip?
  {:doc  "Return true if item is a micro chip."
   :test #(do
            (is (chip? :com))
            (is-not (chip? :cog))
            (is-not (chip? :ele)))}
  [item]
  (contains? all-chips item))

(defn generator?
  {:doc  "Return true if item is a generator."
   :test #(do
            (is-not (generator? :ele))
            (is-not (generator? :com))
            (is (generator? :cog)))}
  [item]
  (contains? all-generators item))

(defn elevator?
  {:doc  "Return true if item is an elevator."
   :test #(do
            (is (elevator? :ele))
            (is-not (elevator? :com))
            (is-not (elevator? :cog)))}
  [item]
  (= item :ele))

(defn floor-safe?
  {:doc  "Return true if floor is safe. A floor is safe if a) there are no
  generators on the floor, or b) for each micro chip on the floor, there
  is a matching generator on the floor."
   :test #(do
            (is (floor-safe? 1 {:ele 1}))
            (is (floor-safe? 1 {:lim 1 :lig 2 :hym 2 :hyg 2 :ele 1}))
            (is (floor-safe? 2 {:lim 1 :lig 2 :hym 2 :hyg 2}))
            (is (floor-safe? 2 {:lim 2 :lig 2 :hym 2 :hyg 2}))
            (is-not (floor-safe? 2 {:lim 1 :lig 2 :hym 2 :hyg 1}))
            (is-not (floor-safe? 2 {:lim 2 :lig 2 :hym 2 :hyg 2 :plm 2 :plg 3})))}
  [floor state]
  (let [items-on-floor (on-floor floor state)
        chips-on-floor (filter chip? items-on-floor)
        required-generators (generators chips-on-floor)
        generators-on-floor (filter generator? items-on-floor)]
    (or (empty? generators-on-floor)
        (every? (set generators-on-floor) required-generators))))

(defn safe?
  {:doc  "Returns true if all floors are safe in the given state."
   :test #(do
            (is (safe? initial-state-example))
            (is (safe? initial-state-puzzle-a))
            (is-not (safe? {:com 1 :plg 1 :cog 2 :plm 3})))}
  [state]
  (every? #(floor-safe? % state) [1 2 3 4]))

(defn goal?
  {:doc  "Return true if we have reached the goal state,
  where all equipment is on the fourth floor."
   :test #(do
            (is (goal? {:com 4 :cog 4}))
            (is-not (goal? initial-state-example)))}
  [state]
  (every? #(= (val %) 4) state))

(defn get-to-floors
  {:doc  "Return the possible floors to go to from the given floor."
   :test #(do
            (is= (get-to-floors 1) [2])
            (is= (get-to-floors 2) [1 3]))}
  [from-floor]
  (nth [[:not-used] [2] [1 3] [2 4] [3]] from-floor))

(defn combinations
  {:doc "Return all possible combinations of floors, and one or two items."}
  [floors items]
  (mapcat (fn [floor]
            (let [singles (for [a items]
                            {:floor floor :items #{a :ele}})
                  doubles (for [a items b items :when (not= a b)]
                            {:floor floor :items #{a b :ele}})]
              (apply conj doubles singles)))
          floors))

(defn get-moves
  {:doc  "Return a sequence of all possible moves, given state."
   :test #(do
            (is= (set (get-moves {:com 1 :ele 1})) #{{:floor 2 :items #{:com :ele}}})
            (is= (set (get-moves {:com 1 :cog 1 :ele 1})) #{{:floor 2 :items #{:com :ele}}
                                                            {:floor 2 :items #{:cog :ele}}
                                                            {:floor 2 :items #{:cog :com :ele}}})
            (is= (set (get-moves {:com 3 :cog 3 :ele 3})) #{{:floor 2 :items #{:com :ele}}
                                                            {:floor 2 :items #{:cog :ele}}
                                                            {:floor 2 :items #{:cog :com :ele}}
                                                            {:floor 4 :items #{:com :ele}}
                                                            {:floor 4 :items #{:cog :ele}}
                                                            {:floor 4 :items #{:cog :com :ele}}})
            )}
  [state]
  (let [from-floor (:ele state)
        to-floors (get-to-floors from-floor)
        items (filter #(not (elevator? %)) (on-floor from-floor state))
        moves (combinations to-floors items)]
    (distinct moves)))

(defn safe-moves
  {:doc  "Return a sequence of all safe moves, given state."
   :test #(do
            (is= (safe-moves {:plg 1 :com 2 :ele 2}) [{:floor 3 :items #{:com :ele}}])
            (is= (safe-moves initial-state-example) [{:floor 2 :items #{:hym :ele}}])
            )}
  [state]
  (filter (fn [move] (safe? (make-move move state))) (get-moves state)))

(defn expand-states
  {:doc "Expand the decorated states in the nested map structure to a sequence."}
  [{state :state parent :parent}]
  (when (some? parent)
    (concat (expand-states parent) [state])))

(defn solve-bfs
  {:doc "Solve puzzle using bredth first search."}
  [frontier explored]
  (let [{state :state parent :parent :as decorated-state} (first frontier)]
    (if (goal? state)
      (count (expand-states decorated-state))
      (let [moves (safe-moves state)
            next-states (map #(make-move % state) moves)
            new-states (filter #(not (contains? explored %)) next-states)
            decorated-states (map (fn [new-state] {:state new-state :parent decorated-state}) new-states)]
        (recur (concat (rest frontier) decorated-states) (conj explored state)))
      )))

(defn solve-dfs
  {:doc "Solve puzzle using depth first search, as a part of an iterative deepening search."}
  [depth max-depth decorated-state]
  (let [state (:state decorated-state)]
    (if (= depth max-depth)
      (when (goal? state)
        (count (expand-states decorated-state)))
      (let [moves (safe-moves state)
            new-states (map #(make-move % state) moves)
            decorated-states (map (fn [new-state] {:state new-state :parent decorated-state}) new-states)]
        (reduce (fn [acc val]
                  (if acc
                    acc
                    (solve-dfs (inc depth) max-depth val)))
                nil
                decorated-states)
        ))))

(defn solve-ids
  {:doc "Solve puzzle using iterative deepening search."}
  [initial-state]
  (let [decorated-state {:state initial-state :parent nil}]
    (->> (iterate inc 0)
         (map (fn [max-depth] (do (println "md:" max-depth) (solve-dfs 0 max-depth decorated-state))))
         (filter some?)
         (first))))

(defn heuristic
  {:doc "Heuristic function for the a-start search."
   :test #(do
            (is= (heuristic {:com 4 :cog 3 :ele 3}) 1)
            (is= (heuristic initial-state-example) 9))}
  [state]
  (->> state
       (filter #(not= (key %) :ele))
       (map #(- 4 (val %)))
       (reduce +)))

(defn solve-a-star
  {:doc "Solve puzzle using a-star search."}
  [initial-state]
  (let [decorated-state {:state initial-state :parent nil :cost 0}]
  ))

(defn solve
  {:test #(do
            ;            (is= (solve initial-state-example) 11)
            (is= (solve {:lig 4 :lim 3 :ele 3}) 1))}
  [initial-state]
  ;(solve-bfs [{:state initial-state :parent nil}] #{})
  (solve-ids initial-state)
  )

;(time (solve initial-state-example))
;(time (solve initial-state-puzzle-a))












(defn day11-puzzle-a
  ;  {:doc  "Solve puzzle A."
  ;  }
  []
  [])
