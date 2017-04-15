(ns aoc.day11
  (:require [clojure.string :refer [ends-with?]])
  (:require [ysera.test :refer [is is-not is=]]))

(def initial-state-example
  {
   :lig 3
   :hyg 2
   :hym 1 :lim 1})

(def initial-state-puzzle-a
  {
   :com 3 :cum 3 :plm 3 :rum 3
   :cog 2 :cug 2 :plg 2 :rug 2
   :prg 1 :prm 1})

(defn floor
  {:doc "Return the floor of item."
   :test #(do
            (is= (floor :a {:a 3 :b 4}) 3))}
  [item state]
  (item state))

(defn on-floor
  {:doc "Return items on floor."
   :test #(do
            (is= (on-floor 1 {:a 3 :b 3 :c 4}) [])
            (is= (on-floor 3 {:a 3 :b 3 :c 4}) [:a :b]))}
  [floor state]
  (sort (map key (filter #(= (val %) floor) state))))

(defn move-to
  {:doc "Move item to floor."
   :test #(do
            (is= (move-to :a 1 {:a 3 :b 4}) {:a 1 :b 4})
            (is= (move-to :a 1 {:a 3 :b 3}) {:a 1 :b 3}))}
  [item floor state]
  (assoc state item floor))

(defn generator
  {:doc "Return the generator matching chip."
   :test #(do
            (is= (generator :plm) :plg))}
  [chip]
  (keyword (str (subs (name chip) 0 2) "g")))

(defn generators
  {:doc "Return a sequence of generators matching the given sequence of chips."
   :test #(do
            (is= (generators [:plm :com]) [:plg :cog]))}
  [chips]
  (map generator chips))

(defn chip
  {:doc "Return the chip matching generator."
   :test #(do
            (is= (chip :plg) :plm))}
  [generator]
  (keyword (str (subs (name generator) 0 2) "m")))

(defn chip?
  {:doc "Return true if item is a micro chip."
   :test #(do
            (is (chip? :aom))
            (is-not (chip? :aog)))}
  [item]
  (ends-with? item "m"))

(defn floor-safe?
  {:doc "Return true if floor is safe. A floor is safe if a) there are no
  generators on the floor, or b) for each micro chip on the floor, there
  is a matching generator on the floor."
   :test #(do
            (is (floor-safe? 1 {}))
            (is (floor-safe? 1 {:com 1 :cog 2 :plm 2 :plg 2}))
            (is (floor-safe? 2 {:com 1 :cog 2 :plm 2 :plg 2}))
            (is (floor-safe? 2 {:com 2 :cog 2 :plm 2 :plg 2}))
            (is-not (floor-safe? 2 {:com 1 :cog 2 :plm 2 :plg 1}))
            (is-not (floor-safe? 2 {:com 2 :cog 2 :plm 2 :plg 2 :alm 2 :alg 3})))}
  [floor state]
  (let [items-on-floor (on-floor floor state)
        chips-on-floor (filter chip? items-on-floor)
        required-generators (generators chips-on-floor)
        generators-on-floor (filter #(not (chip? %)) items-on-floor)]
    (or (empty? generators-on-floor)
        (every? (set generators-on-floor) required-generators))))

(defn safe?
  {:doc "Returns true if all floors are safe in the given state."
   :test #(do
            (is (safe? initial-state-example))
            (is (safe? initial-state-puzzle-a))
            (is-not (safe? {:com 1 :plg 1 :cog 2 :plm 3})))}
  [state]
  (every? #(floor-safe? % state) [1 2 3 4]))

(defn goal?
  {:doc "Return true if we have reached the goal state,
  where all equipment is on the fourth floor."
   :test #(do
            (is (goal? {:com 4 :cog 4}))
            (is-not (goal? initial-state-example)))}
  [state]
  (and (every? #(empty? (on-floor % state)) [1 2 3])
       (not (empty? (on-floor 4 state)))))





(defn day11-puzzle-a
  ;  {:doc  "Solve puzzle A."
  ;  }
  []
  [])
