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
  {:test #(do
            (is= (pos-char "1234567") [6 \7]))}
  [coll]
  (println coll)
  [(- (int (nth coll 5)) (int \0)) (nth coll 6)])

(defn valid-pos?
  {:test #(do
            (is (valid-pos? [0 \8]))
            (is (valid-pos? [7 \e]))
            (is-not (valid-pos? [8 \1])))}
  [pc]
  (< (first pc) 8))

(defn valid-pos-chars
  {:test #(do
            (is= (valid-pos-chars ["0000015" "00000a6"]) [[1 \5]]))}
  [hashes]
  (->> hashes
       (map pos-char)
       (filter valid-pos?)))

(defn find-chars
  ;  {:test #(do
  ;            (is= (find-chars "abc" 2) {1 \5 4 \e}))}
  [door-id num]
  (letfn [(iter [s res]
            (if (= (count res) num)
              res
              (let [[pos char] (first s)]
                (recur (rest s) (merge {pos char} res)))))]
    (iter (valid-pos-chars (pw-hashes door-id)) {})))

(defn map-to-str
  {:test #(do
            (is= (map-to-str {1 \b 3 \d 5 \f 7 \h 0 \a 2 \c 4 \e 6 \g}) "abcdefgh"))}
  [m]
  (apply str (map val (sort m))))

(defn make-pw-b
  ;  {:test #(do
  ;            (is= (make-pw-b "abc") "05ace8e3"))}
  [door-id]
  (map-to-str (find-chars door-id 8)))

(defn day05-task-b
  {:doc  "Solve task B."
   :test (fn []
           (is= (day05-task-b) "8c35d1ab"))}
  []
  (make-pw-b "ffykfhsq"))
