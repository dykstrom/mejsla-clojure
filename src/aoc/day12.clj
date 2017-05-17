(ns aoc.day12
  (:require [ysera.test :refer [is is-not is=]])
  (:refer-clojure :exclude [inc dec]))

;; State consists of registers a, b, c, and d, and a program counter p
(def initial-state {:a 0 :b 0 :c 0 :d 0 :p 0})

(defn inc
  {:doc  "Increase REGISTER by one."
   :test #(do
            (is= (inc {:a 0 :b 0} :a) {:a 1 :b 0}))}
  [state register]
  (assoc state register (+ (get state register) 1)))

(defn dec
  {:test #(do
            (is= (dec {:a 5 :b 3} :b) {:a 5 :b 2}))}
  [state register]
  (assoc state register (- (get state register) 1)))

(defn cpy
  {:test #(do
            (is= (cpy {:a 1 :b 2 :c 3} 4 :b) {:a 1 :b 4 :c 3})
            (is= (cpy {:a 1 :b 2 :c 3} :a :b) {:a 1 :b 1 :c 3}))}
  [state source dest]
  (let [value (if (integer? source) source (get state source))]
    (assoc state dest value)))

(defn jnz
  {:doc  "Jump if SOURCE is not zero. SOURCE may be an integer or a register symbol,
and so may OFFSET. If OFFSET is positive, jump forward. If OFFSET is negative, jump
backward. This function assumes that the program counter has been increased by one
after reading the instruction, so it actually jumps to PC + OFFSET - 1."
   :test #(do
            (is= (jnz {:a 1 :b 2 :p 1} 4 3) {:a 1 :b 2 :p 3})
            (is= (jnz {:a 1 :b 2 :p 1} 4 -3) {:a 1 :b 2 :p -3})
            (is= (jnz {:a 1 :b 2 :p 1} :a 2) {:a 1 :b 2 :p 2})
            (is= (jnz {:a 0 :b 2 :p 1} :a 2) {:a 0 :b 2 :p 1})
            (is= (jnz {:a 0 :b 2 :p 1} 1 :b) {:a 0 :b 2 :p 2}))}
  [state source offset]
  (let [value (if (integer? source) source (get state source))
        jump (if (integer? offset) offset (get state offset))]
    (if (= value 0)
      state
      ;; The -1 is because the PC has been incremented between
      ;; reading and executing the instruction
      (assoc state :p (+ (get state :p) jump -1)))))

(defn read-instruction
  {:test #(do
            (is= (read-instruction {:a 5 :p 0} [[cpy 41 :a] [inc :a]])
                 [cpy 41 :a])
            (is= (read-instruction {:a 5 :p 1} [[cpy 41 :a] [inc :a]])
                 [inc :a]))}
  [state instructions]
  (nth instructions (get state :p)))

(defn execute-instruction
  {:test #(do
            (is= (execute-instruction {:a 5 :p 0} [cpy 41 :a]) {:a 41 :p 0})
            (is= (execute-instruction {:a 5 :b 7 :p 0} [cpy :a :b]) {:a 5 :b 5 :p 0})
            (is= (execute-instruction {:a 5 :p 0} [jnz :a 2]) {:a 5 :p 1}))}
  [state instruction]
  (let [func (first instruction)
        args (rest instruction)]
    (apply func state args)))

(defn read-increment-execute
  {:test #(do
            (is= (read-increment-execute {:a 5 :b 3 :p 0} [[cpy 41 :a] [inc :a]])
                 {:a 42 :b 3 :p 2})
            (is= (read-increment-execute {:a 5 :b 3 :p 0} [[cpy 41 :a] [jnz :a 2] [inc :a] [inc :b]])
                 {:a 41 :b 4 :p 4}))}
  [state instructions]
  (let [len (count instructions)]
    (first (drop-while #(< (get % :p) len)
                       (iterate #(let [instruction (read-instruction % instructions)
                                       incremented (inc % :p)]
                                   (execute-instruction incremented instruction))
                                state)))))

(def example-input [[cpy 41 :a]
                    [inc :a]
                    [inc :a]
                    [dec :a]
                    [jnz :a 2]
                    [dec :a]])

(def puzzle-input [[cpy 1 :a]
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
                   [jnz :c -5]])

(defn solve-example
  []
  (read-increment-execute initial-state example-input))

(defn solve-puzzle-a
  []
  (read-increment-execute initial-state puzzle-input))

(defn solve-puzzle-b
  []
  (let [puzzle-b-state (assoc initial-state :c 1)]
    (read-increment-execute puzzle-b-state puzzle-input)))

;; (time (solve-example))
;; (time (solve-puzzle-a))
;; (time (solve-puzzle-b))
