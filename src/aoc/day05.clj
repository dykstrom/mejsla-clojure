(ns aoc.day05
  (:require [clojure.string :refer [starts-with?]])
  (:require [ysera.test :refer [is is-not is=]])
  (:require [digest]))

(defn pw-hash?
  {:doc "Return true if the given MD5 hash represents a password char."
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

(defn make-pw
;  {:test #(do
;            (is= (make-pw "abc") "18f47a30"))}
  [door-id]
  (->> (pw-hashes door-id)
       (map sixth)
       (take 8)
       (apply str)))

(defn day05-task-a
  {:doc  "Solve task A."
   :test (fn []
           (is= (day05-task-a) "c6697b55"))}
  []
  (make-pw "ffykfhsq"))
