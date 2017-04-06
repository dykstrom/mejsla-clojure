(ns aoc.day04
  (:require [clojure.string :refer [split-lines]])
  (:require [ysera.test :refer [is is-not is=]]))

(defn freq-keyfn
  [entry]
  [(- (val entry)) (key entry)])

(defn calculate-checksum
  [chars]
  (->> chars
       (map first)
       (take 5)
       (apply str)))

(defn real?
  {:test (fn []
           (is (real? "aaaaa-bbb-z-y-x-123[abxyz]"))
           (is-not (real? "totally-real-room-200[decoy]")))}
  [room]
  (let [name (first (re-seq (re-pattern "[-a-z]+") room))
        checksum (second (first (re-seq (re-pattern "\\[([a-z]+)\\]") room)))
        chars (clojure.string/replace name "-" "")
        freq (frequencies chars)
        sorted (sort-by freq-keyfn freq)
        calculated-checksum (calculate-checksum sorted)]
  (= checksum calculated-checksum)))

(defn sector-id
  {:test (fn []
           (is= (sector-id "aaaaa-bbb-z-y-x-123[abxyz]") "123"))}
  [room]
  (first (re-seq (re-pattern "[0-9]+") room)))

(defn count-real
  {:test (fn []
           (is= (count-real ["aaaaa-bbb-z-y-x-123[abxyz]"
                             "a-b-c-d-e-f-g-h-987[abcde]"
                             "not-a-real-room-404[oarel]"
                             "totally-real-room-200[decoy]"])
                1514))}
  [rooms]
  (->> rooms
       (filter real?)
       (map sector-id)
       (map read-string)
       (reduce + 0)))

(defn read-rooms
  [filename]
  (split-lines (slurp filename)))

(defn day04-task-a
  {:doc  "Solve task A."
   :test (fn []
           (is= (day04-task-a) 185371))}
  []
  (count-real (read-rooms "resources/rooms.txt")))
