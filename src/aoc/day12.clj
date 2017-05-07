(ns aoc.day12
  (:require [ysera.test :refer [is is-not is=]])
  (:refer-clojure :exclude [inc dec]))

;; State consists of registers a, b, c, and d, and a program counter p
(def initial-state {:a 0 :b 0 :c 0 :d 0 :p 0})

(defn updated
  [state key value]
  (assoc state key val))

(defn inc
  [state register]
  (updated state register (+ 1 (get state register))))

(defn dec
  [state register]
  (updated state register (- 1 (get state register))))

(defn cpy
  [state source dest]
  (let [value (if (integer? source) source (get state source))]
    (updated state dest value)))

(defn jnz
  [state source offset]
  (let [value (if (integer? source) source (get state source))]
    (if (= value 0)
      state
      ;; The -1 is because the PC has been incremented between
      ;; reading and executing the instruction
      (updated state :p (+ (get state :p) offset -1)))))

(defn read-instruction
  [state instructions]
  (nth instructions (get state :p)))

(defn execute-instruction
  [state instruction]
  (let [func (first instruction)
        args (rest instruction)]
    (apply func state args)))

(defn read-increment-execute
  [state instructions]
  (println state)
  (println instructions)
  (let [instruction (read-instruction state instructions)]
    (println instruction)
    (if (nil? instruction)
      state
      (let [new-state (execute-instruction (inc state :p) instruction)]
        (read-increment-execute new-state instructions)))))

(defn solve-example
  []
  (read-increment-execute initial-state '[[cpy 41 :a]
                                         [inc :a]
                                         [inc :a]
                                         [dec :a]
                                         [jnz :a 2]
                                         [dec :a]]))

(defn solve-puzzle-a
  []
  (read-increment-execute initial-state [[cpy 1 :a]
                                         [cpy 1 :b]
                                         [cpy 26 :d]
                                         [jnz :c 2]
                                         [jnz 1 5]
                                         [cpy 7 :c]
                                         [inc :d]
                                         [dec :c]
                                         [jnz :c -2]
                                         [cpy :a :c]
                                         [inc :a]
                                         [dec :b]
                                         [jnz :b -2]
                                         [cpy :c :b]
                                         [dec :d]
                                         [jnz :d -6]
                                         [cpy 19 :c]
                                         [cpy 14 :d]
                                         [inc :a]
                                         [dec :d]
                                         [jnz :d -2]
                                         [dec :c]
                                         [jnz :c -5]]))

(read-increment-execute initial-state '[[cpy 41 :a]])
;; (solve-example)
;; (solve-puzzle-a)
