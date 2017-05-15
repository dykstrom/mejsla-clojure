(ns aoc.day23
  (:require [ysera.test :refer [is is-not is=]])
  (:refer-clojure :exclude [inc dec]))

;; State consists of registers a, b, c, and d, and a program counter p
(def initial-state {:a 0 :b 0 :c 0 :d 0 :p 0})

(declare toggle-map)

(defn inc
  {:test #(do
            (is= (inc {:a 0 :b 0} nil :a) [{:a 1 :b 0} nil]))}
  [state instructions register]
  (if (not (keyword? register))
    [state instructions]
    [(assoc state register (+ (get state register) 1)) instructions]))

(defn dec
  {:test #(do
            (is= (dec {:a 5 :b 3} nil :b) [{:a 5 :b 2} nil]))}
  [state instructions register]
  (if (not (keyword? register))
    [state instructions]
    [(assoc state register (- (get state register) 1)) instructions]))

(defn mul
  {:doc  "Multiply SOURC and DEST, and store the result in DEST. DEST must be a register."
   :test #(do
            (is= (mul {:a 5 :b 3} nil :a :b) [{:a 5 :b 15} nil])
            (is= (mul {:a 0 :b 1 :c 7} nil :a :c) [{:a 0 :b 1 :c 0} nil]))}
  [state instructions source dest]
  (if (not (keyword? dest))
    [state instructions]
    (let [source-value (if (integer? source) source (get state source))
          dest-value (get state dest)]
      [(assoc state dest (* source-value dest-value)) instructions])))

(defn cpy
  {:test #(do
            (is= (cpy {:a 1 :b 2 :c 3} nil 4 :b) [{:a 1 :b 4 :c 3} nil])
            (is= (cpy {:a 1 :b 2 :c 3} nil :a :b) [{:a 1 :b 1 :c 3} nil]))}
  [state instructions source dest]
  (if (not (keyword? dest))
    [state instructions]
    (let [value (if (integer? source) source (get state source))]
      [(assoc state dest value) instructions])))

(defn jnz
  {:doc  "Jump if SOURCE is not zero. SOURCE may be an integer or a register symbol,
and so may OFFSET. If OFFSET is positive, jump forward. If OFFSET is negative, jump
backward. This function assumes that the program counter has been increased by one
after reading the instruction, so it actually jumps to PC + OFFSET - 1."
   :test #(do
            (is= (jnz {:a 1 :b 2 :p 1} nil 4 3) [{:a 1 :b 2 :p 3} nil])
            (is= (jnz {:a 1 :b 2 :p 1} nil 4 -3) [{:a 1 :b 2 :p -3} nil])
            (is= (jnz {:a 1 :b 2 :p 1} nil :a 2) [{:a 1 :b 2 :p 2} nil])
            (is= (jnz {:a 0 :b 2 :p 1} nil :a 2) [{:a 0 :b 2 :p 1} nil])
            (is= (jnz {:a 0 :b 2 :p 1} nil 1 :b) [{:a 0 :b 2 :p 2} nil]))}
  [state instructions source offset]
  (let [value (if (integer? source) source (get state source))
        jump (if (integer? offset) offset (get state offset))]
    (if (= value 0)
      [state instructions]
      ;; The -1 is because the PC has been incremented between
      ;; reading and executing the instruction
      [(assoc state :p (+ (get state :p) jump -1)) instructions])))

(defn tgl-toggle-instruction
  [address instruction instructions]
  (let [toggled (cons (get toggle-map (first instruction)) (vec (rest instruction)))]
    (assoc instructions address toggled)))

(defn tgl
  {:doc  "See http://adventofcode.com/2016/day/23"
   :test #(do
            (is= (tgl {:a 0 :p 1} [[tgl 2] [dec :a] [cpy 1 :a]] 2)
                 [{:a 0 :p 1} [[tgl 2] [dec :a] [jnz 1 :a]]])
            (is= (tgl {:a 0 :p 1} [[tgl 1] [dec :a] [cpy 1 :a]] 1)
                 [{:a 0 :p 1} [[tgl 1] [inc :a] [cpy 1 :a]]])
            (is= (tgl {:a 0 :p 1} [[tgl 0] [dec :a] [cpy 1 :a]] 0)
                 [{:a 0 :p 1} [[inc 0] [dec :a] [cpy 1 :a]]]))}
  [state instructions offset]
  (let [relative (if (integer? offset) offset (get state offset))
        address (+ (get state :p) relative -1)
        instruction (nth instructions address nil)]
    (if (nil? instruction)
      [state instructions]
      [state (tgl-toggle-instruction address instruction instructions)])))

;; Defines how function tgl toggles functions
(def toggle-map {inc dec
                 dec inc
                 tgl inc
                 jnz cpy
                 cpy jnz})

(defn read-instruction
  {:test #(do
            (is= (read-instruction {:a 5 :p 0} [[cpy 41 :a] [inc :a]]) [cpy 41 :a])
            (is= (read-instruction {:a 5 :p 1} [[cpy 41 :a] [inc :a]]) [inc :a]))}
  [state instructions]
  (nth instructions (get state :p)))

(defn execute-instruction
  {:test #(do
            (is= (execute-instruction {:a 5 :p 0} nil [cpy 41 :a]) [{:a 41 :p 0} nil])
            (is= (execute-instruction {:a 5 :p 0} nil [inc :a]) [{:a 6 :p 0} nil])
            (is= (execute-instruction {:a 5 :b 7 :p 0} nil [cpy :a :b]) [{:a 5 :b 5 :p 0} nil])
            (is= (execute-instruction {:a 5 :p 0} nil [jnz :a 2]) [{:a 5 :p 1} nil])
            ;; Invalid instructions
            (is= (execute-instruction {:a 5 :p 0} nil [cpy 1 2]) [{:a 5 :p 0} nil])
            (is= (execute-instruction {:a 5 :p 0} nil [inc 1]) [{:a 5 :p 0} nil]))}
  [state instructions instruction]
  (let [func (first instruction)
        args (rest instruction)]
    (apply func state instructions args)))

(defn read-increment-execute
  {:test #(do
            (is= (read-increment-execute {:a 5 :b 3 :p 0} [[cpy 41 :a] [inc :a]])
                 {:a 42 :b 3 :p 2})
            (is= (read-increment-execute {:a 5 :b 3 :p 0} [[cpy 41 :a] [jnz :a 2] [inc :a] [inc :b]])
                 {:a 41 :b 4 :p 4}))}
  [state instructions]
  (if (>= (get state :p) (count instructions))
    state
    (let [instruction (read-instruction state instructions)
          [incremented _] (inc state instructions :p)
          [new-state new-instructions] (execute-instruction incremented instructions instruction)]
      (recur new-state new-instructions))))

(def example-12-input [[cpy 41 :a]
                       [inc :a]
                       [inc :a]
                       [dec :a]
                       [jnz :a 2]
                       [dec :a]])

(def example-23-input [[cpy 2 :a]
                       [tgl :a]
                       [tgl :a]
                       [tgl :a]
                       [cpy 1 :a]
                       [dec :a]
                       [dec :a]])

(def puzzle-input [[cpy :a :b]
                   [dec :b]
                   [cpy :a :d]
                   [cpy 0 :a]
                   [cpy :b :c]
                   [inc :a]
                   [dec :c]
                   [jnz :c -2]
                   [dec :d]
                   [jnz :d -5]
                   [dec :b]
                   [cpy :b :c]
                   [cpy :c :d]
                   [dec :d]
                   [inc :c]
                   [jnz :d -2]
                   [tgl :c]
                   [cpy -16 :c]
                   [jnz 1 :c]
                   [cpy 98 :c]
                   [jnz 86 :d]
                   [inc :a]
                   [inc :d]
                   [jnz :d -2]
                   [inc :c]
                   [jnz :c -5]])

(defn solve-example-12
  []
  (read-increment-execute initial-state example-12-input))

(defn solve-example-23
  []
  (read-increment-execute initial-state example-23-input))

(defn solve-puzzle-a
  []
  (let [puzzle-a-state (assoc initial-state :a 7)]
    (read-increment-execute puzzle-a-state puzzle-input)))

(defn solve-puzzle-b
  []
  (let [puzzle-b-state (assoc initial-state :a 12)]
    (read-increment-execute puzzle-b-state puzzle-input)))

;; (time (solve-example-12))
;; (time (solve-example-23))
;; (time (solve-puzzle-a))
;; (time (solve-puzzle-b))      ;; --> 479010028
