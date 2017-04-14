(ns aoc.day05
  (:require [clojure.string :refer [starts-with?]])
  (:require [ysera.test :refer [is is-not is=]])
  (:require [digest]))

(defn pw-hash?
  {:doc  "Return true if the given MD5 hash represents a password char."
   :test #(do
            (is-not (pw-hash? "577571be4d"))
            (is (pw-hash? "00000155f8")))}
  [hash]
  (starts-with? hash "00000"))

(defn make-hash
  {:doc "Return the MD5 hash of the door-id concatenated with the index."}
  [door-id index]
  (digest/md5 (str door-id index)))

(defn pw-hashes
  {:doc "Return a lazy sequence of password hashes created from the door-id
  and an increasing index."}
  [door-id]
  (->> (iterate inc 0)
       (map (partial make-hash door-id))
       (filter pw-hash?)))

(defn sixth
  {:test #(do
            (is= (sixth "123456") \6))}
  [coll]
  (nth coll 5))

(defn make-pw-a
  ;  {:test #(do
  ;            (is= (make-pw-a "abc") "18f47a30"))}
  [door-id]
  (->> (pw-hashes door-id)
       (map sixth)
       (take 8)
       (apply str)))

(defn day05-task-a
  ;  {:doc  "Solve task A."
  ;   :test (fn []
  ;           (is= (day05-task-a) "c6697b55"))}
  []
  (make-pw-a "ffykfhsq"))

(defn pos-char
  {:doc  "Extract the position and password character from hash."
   :test #(do
            (is= (pos-char "1234567") [6 \7]))}
  [hash]
  (println hash)
  [(- (int (nth hash 5)) (int \0)) (nth hash 6)])

(defn valid-pos?
  {:doc  "Return true if the position of pc is valid."
   :test #(do
            (is (valid-pos? [0 \8]))
            (is (valid-pos? [7 \e]))
            (is-not (valid-pos? [8 \1])))}
  [pc]
  (< (first pc) 8))

(defn valid-pos-chars
  {:doc  "Return a sequence of valid position-character vectors extracted
  from the given sequence of hashes."
   :test #(do
            (is= (valid-pos-chars ["0000015" "00000a6"]) [[1 \5]]))}
  [hashes]
  (->> hashes
       (map pos-char)
       (filter valid-pos?)))

(defn find-chars
  {:doc "Return a map of the num first valid position-characters found in hashes."
   :test #(do
            (is= (find-chars 3 ["00000c6" "000006a" "0000051" "0000063" "0000025" "0000047"])
                 {2 \5 5 \1 6 \a}))}
  [num hashes]
  (->> (valid-pos-chars hashes)
       (reduce (fn [acc [pos char]]
                 (if (= (count acc) num) (reduced acc) (merge {pos char} acc)))
               {})))

(defn map-to-str
  {:doc "Convert a map of position-character entries to a string with each character
  in the correct position."
   :test #(do
            (is= (map-to-str {1 \b 3 \d 5 \f 7 \h 0 \a 2 \c 4 \e 6 \g}) "abcdefgh"))}
  [m]
  (apply str (map val (sort m))))

(defn make-pw-b
  ;  {:test #(do
  ;            (is= (make-pw-b "abc") "05ace8e3"))}
  [door-id]
  (->> (pw-hashes door-id)
       (find-chars 8)
       (map-to-str)))

(defn day05-task-b
  {:doc  "Solve task B."
   :test (fn []
           (is= (day05-task-b) "8c35d1ab"))}
  []
  (make-pw-b "ffykfhsq"))
