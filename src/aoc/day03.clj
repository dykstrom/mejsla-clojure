(ns aoc.day03
  (:require [clojure.string :refer [split split-lines trim]])
  (:require [ysera.test :refer [is=]]))

(defn possible?
  {:doc  "Returns true if this is a possible triangle."
   :test (fn []
           (is= (possible? [5 10 25]) false)
           (is= (possible? [25 7 8]) false)
           (is= (possible? [15 13 25]) true)
           (is= (possible? [39 20 20]) true))}
  [triangle]
  (let [[a b c] triangle]
    (and (> (+ a b) c)
         (> (+ a c) b)
         (> (+ b c) a))))

(defn count-possible
  {:doc  "Counts the number of possible triangles in the sequence."
   :test (fn []
           (is= (count-possible []) 0)
           (is= (count-possible [[5 10 25]]) 0)
           (is= (count-possible [[2 3 4] [5 10 25]]) 1)
           (is= (count-possible [[2 3 4] [5 10 12]]) 2))}
  [triangles]
  (->> triangles
       (filter possible?)
       count))

(defn string-to-seq
  {:doc  "Convert a string of three numbers to a sequence of three numbers."
   :test (fn []
           (is= (string-to-seq "1 2 3") [1 2 3])
           (is= (string-to-seq " 10  20  30  ") [10 20 30]))}
  [line]
  (map read-string (split (trim line) #" +")))

(defn read-triangles
  {:doc "Read triangles from file and return a sequence of triplets."}
  [filename]
  (->> (slurp filename)
       split-lines
       (map string-to-seq)))

(defn day03-task-a
  {:doc  "Solve task A."
   :test (fn []
           (is= (day03-task-a) 869))}
  []
  (count-possible (read-triangles "resources/triangles.txt")))

(defn transpose
  {:doc  "Transpose the given NxN matrix."
   :test (fn []
           (is= (transpose [[1 2] [3 4]]) [[1 3] [2 4]])
           (is= (transpose [[1 2 3] [4 5 6] [7 8 9]]) [[1 4 7] [2 5 8] [3 6 9]]))}
  [matrix]
  (apply map vector matrix))

(defn transpose-3-by-3
  {:doc "Transpose a sequence of triplets by taking three triplets a time
  and transposing the resulting matrix."
   :test (fn []
           (is= (transpose-3-by-3 [[1 2 3]
                                   [4 5 6]
                                   [7 8 9]])
                [[1 4 7]
                 [2 5 8]
                 [3 6 9]])
           (is= (transpose-3-by-3 [[101 301 501]
                                   [102 302 502]
                                   [103 303 503]
                                   [201 401 601]
                                   [202 402 602]
                                   [203 403 603]])
                [[101 102 103]
                 [301 302 303]
                 [501 502 503]
                 [201 202 203]
                 [401 402 403]
                 [601 602 603]]))}
  [triangles]
  (->> triangles
       (partition 3)
       (map transpose)
       (mapcat identity)))

(defn day03-task-b
  {:doc  "Solve task B."
   :test (fn []
           (is= (day03-task-b) 1544))}
  []
  (->> (read-triangles "resources/triangles.txt")
       transpose-3-by-3
       count-possible))
