(ns aoc.day01
  (:require [ysera.test :refer [is=]]))

; Map a turn to a map of source and dest compass directions if you turn that way
(def turn-to-dir {:L {:N :W :E :N :S :E :W :S} :R {:N :E :E :S :S :W :W :N}})

; Map a direction to a move vector
(def dir-to-delta {:N [0 1] :E [1 0] :S [0 -1] :W [-1 0]})

(defn parse-command
  {:test (fn []
           (is= (parse-command "R1") {:turn :R :dist 1})
           (is= (parse-command "L15") {:turn :L :dist 15}))}
  [text]
  {:turn (keyword (subs text 0 1)) :dist (read-string (subs text 1))})

(defn make-turn
  {:doc  "Start facing in the direction of init and turn as given by command.
  Return resulting direction with distance of command."
   :test (fn []
           (is= (make-turn {:dir :N :dist 0} {:turn :R :dist 2}) {:dir :E :dist 2})
           (is= (make-turn {:dir :W :dist 0} {:turn :L :dist 5}) {:dir :S :dist 5}))}
  [init command]
  {:dir  (get (get turn-to-dir (:turn command)) (:dir init))
   :dist (:dist command)})

(defn make-turns
  {:test (fn []
           (is= (make-turns :N [{:turn :R :dist 2} {:turn :L :dist 3}]) [{:dir :E :dist 2} {:dir :N :dist 3}]))}
  [dir commands]
  (rest (reductions make-turn {:dir :N :dist 0} commands)))

(defn move-step
  {:test (fn []
           (is= (move-step {:x 0 :y 0} {:dir :N :dist 5}) {:x 0 :y 5}))}
  [start move]
  (let [[dx dy] (get dir-to-delta (:dir move))]
    {:x (+ (:x start) (* dx (:dist move)))
     :y (+ (:y start) (* dy (:dist move)))}))

(defn move-path
  {:doc  "Move from the start position as instructed by the commands."
   :test (fn []
           (is= (move-path {:x 0 :y 0} ["R2" "L3"]) {:x 2 :y 3}))}
  [start commands]
  (reduce move-step start (make-turns :N (map parse-command commands))))

(defn abs [n] (max n (- n)))

(defn distance-a
  {:doc "Calculate taxi distance for task A."
   :test (fn []
           (is= (distance-a ["R2", "L3"]) 5)
           (is= (distance-a ["R2", "R2", "R2"]) 2)
           (is= (distance-a ["R5", "L5", "R5", "R3"]) 12))}
  [commands]
  (let [start {:x 0 :y 0}
        end (move-path start commands)]
    (+ (abs (- (:x start) (:x end))) (abs (- (:y start) (:y end))))))

(distance-a ["L4", "L1", "R4", "R1", "R1", "L3", "R5", "L5", "L2", "L3", "R2", "R1", "L4", "R5", "R4", "L2", "R1", "R3",
           "L5", "R1", "L3", "L2", "R5", "L4", "L5", "R1", "R2", "L1", "R5", "L3", "R2", "R2", "L1", "R5", "R2", "L1",
           "L1", "R2", "L1", "R1", "L2", "L2", "R4", "R3", "R2", "L3", "L188", "L3", "R2", "R54", "R1", "R1", "L2", "L4",
           "L3", "L2", "R3", "L1", "L1", "R3", "R5", "L1", "R5", "L1", "L1", "R2", "R4", "R4", "L5", "L4", "L1", "R2",
           "R4", "R5", "L2", "L3", "R5", "L5", "R1", "R5", "L2", "R4", "L2", "L1", "R4", "R3", "R4", "L4", "R3", "L4",
           "R78", "R2", "L3", "R188", "R2", "R3", "L2", "R2", "R3", "R1", "R5", "R1", "L1", "L1", "R4", "R2", "R1", "R5",
           "L1", "R4", "L4", "R2", "R5", "L2", "L5", "R4", "L3", "L2", "R1", "R1", "L5", "L4", "R1", "L5", "L1", "L5",
           "L1", "L4", "L3", "L5", "R4", "R5", "R2", "L5", "R5", "R5", "R4", "R2", "L1", "L2", "R3", "R5", "R5", "R5",
           "L2", "L1", "R4", "R3", "R1", "L4", "L2", "L3", "R2", "L3", "L5", "L2", "L2", "L1", "L2", "R5", "L2", "L2",
           "L3", "L1", "R1", "L4", "R2", "L4", "R3", "R5", "R3", "R4", "R1", "R5", "L3", "L5", "L5", "L3", "L2", "L1",
           "R3", "L4", "R3", "R2", "L1", "R3", "R1", "L2", "R4", "L3", "L3", "L3", "L1", "L2"])
