(ns aoc.day11
  (:require [clojure.string :refer [ends-with?]])
  (:require [ysera.test :refer [is is-not is=]]))

(def initial-state-example
  {
   :lig 3
   :hyg 2
   :hym 1 :lim 1 :ele 1})

(def initial-state-puzzle-a
  {
   :com 3 :cum 3 :plm 3 :rum 3
   :cog 2 :cug 2 :plg 2 :rug 2
   :prg 1 :prm 1 :ele 1})

(defn floor
  {:doc  "Return the floor of item."
   :test #(do
            (is= (floor :a {:a 3 :b 4}) 3))}
  [item state]
  (item state))

(defn on-floor
  {:doc  "Return items on floor."
   :test #(do
            (is= (on-floor 1 {:a 3 :b 3 :c 4}) [])
            (is= (on-floor 3 {:a 3 :b 3 :c 4}) [:a :b]))}
  [floor state]
  (sort (map key (filter #(= (val %) floor) state))))

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
            (is= (make-move {:floor 2 :items [:ceg :cem :ele]} {:ceg 1 :cem 1 :ele 1})
                 {:ceg 2 :cem 2 :ele 2})
            (is= (make-move {:floor 2 :items [:ceg :ele]} {:ceg 1 :cem 1 :ele 1})
                 {:ceg 2 :cem 1 :ele 2}))}
  [move state]
  (reduce (fn [acc item] (move-to item (:floor move) acc))
          state
          (:items move)))

(defn generator
  {:doc  "Return the generator matching chip."
   :test #(do
            (is= (generator :plm) :plg))}
  [chip]
  (keyword (str (subs (name chip) 0 2) "g")))

(defn generators
  {:doc  "Return a sequence of generators matching the given sequence of chips."
   :test #(do
            (is= (generators [:plm :com]) [:plg :cog]))}
  [chips]
  (map generator chips))

(defn chip
  {:doc  "Return the chip matching generator."
   :test #(do
            (is= (chip :plg) :plm))}
  [generator]
  (keyword (str (subs (name generator) 0 2) "m")))

(defn chip?
  {:doc  "Return true if item is a micro chip."
   :test #(do
            (is (chip? :aom))
            (is-not (chip? :aog))
            (is-not (chip? :ele)))}
  [item]
  (ends-with? item "m"))

(defn generator?
  {:doc  "Return true if item is a generator."
   :test #(do
            (is-not (generator? :ele))
            (is-not (generator? :aom))
            (is (generator? :aog)))}
  [item]
  (ends-with? item "g"))

(defn elevator?
  {:doc  "Return true if item is an elevator."
   :test #(do
            (is (elevator? :ele))
            (is-not (elevator? :aom))
            (is-not (elevator? :aog)))}
  [item]
  (= item :ele))

(defn floor-safe?
  {:doc  "Return true if floor is safe. A floor is safe if a) there are no
  generators on the floor, or b) for each micro chip on the floor, there
  is a matching generator on the floor."
   :test #(do
            (is (floor-safe? 1 {:ele 1}))
            (is (floor-safe? 1 {:com 1 :cog 2 :plm 2 :plg 2 :ele 1}))
            (is (floor-safe? 2 {:com 1 :cog 2 :plm 2 :plg 2}))
            (is (floor-safe? 2 {:com 2 :cog 2 :plm 2 :plg 2}))
            (is-not (floor-safe? 2 {:com 1 :cog 2 :plm 2 :plg 1}))
            (is-not (floor-safe? 2 {:com 2 :cog 2 :plm 2 :plg 2 :alm 2 :alg 3})))}
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
  (every? #(empty? (on-floor % state)) [1 2 3]))

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
                            {:floor floor :items (sort [a :ele])})
                  doubles (for [a items b items :when (not= a b)]
                            {:floor floor :items (sort [a b :ele])})]
              (apply conj doubles singles)))
          floors))

(defn get-moves
  {:doc  "Return a sequence of all possible moves, given state."
   :test #(do
            (is= (get-moves {:com 1 :ele 1}) [{:floor 2 :items [:com :ele]}])
            (is= (get-moves {:com 1 :cog 1 :ele 1}) [{:floor 2 :items [:cog :ele]}
                                                     {:floor 2 :items [:com :ele]}
                                                     {:floor 2 :items [:cog :com :ele]}])
            (is= (get-moves {:com 3 :cog 3 :ele 3}) [{:floor 2 :items [:cog :ele]}
                                                     {:floor 4 :items [:cog :ele]}
                                                     {:floor 2 :items [:com :ele]}
                                                     {:floor 4 :items [:com :ele]}
                                                     {:floor 2 :items [:cog :com :ele]}
                                                     {:floor 4 :items [:cog :com :ele]}])
            )}
  [state]
  (let [from-floor (floor :ele state)
        to-floors (get-to-floors from-floor)
        items (filter #(not (elevator? %)) (on-floor from-floor state))
        moves (combinations to-floors items)]
      (distinct (sort-by #(vec (:items %)) moves)))) ; Sort by items and make unique

(defn safe-moves
  {:doc "Return a sequence of all safe moves, given state."
   :test #(do
            (is= (safe-moves {:plg 1 :com 2 :ele 2}) [{:floor 3 :items [:com :ele]}])
            (is= (safe-moves initial-state-example) [{:floor 2 :items [:ele :hym]}])
            )}
  [state]
  (filter (fn [move] (safe? (make-move move state))) (get-moves state)))

(defn solve
  {:test #(do
            (is= (solve initial-state-example []) []))}
  [state moves-made]
  (if (goal? state)
    moves-made
    (let [moves (safe-moves state)]
      (println "state: " state ", moves: " moves)
    )))














(defn day11-puzzle-a
  ;  {:doc  "Solve puzzle A."
  ;  }
  []
  [])
