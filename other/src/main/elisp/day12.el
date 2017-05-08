;; Advent of code 2016, day 12

(defconst initial-state (list (cons 'a 0)
							  (cons 'b 0)
							  (cons 'c 0)
							  (cons 'd 0)
							  (cons 'p 0))
  "State consists of registers a, b, c, and d, and a program counter p.")

(defun get (state key)
  (cdr (assoc key state)))

(defun updated (state key value)
  (cons (cons key value) state))

(defun inc (state register)
  (updated state register (1+ (get state register))))

(defun dec (state register)
  (updated state register (1- (get state register))))

(defun cpy (state source dest)
  (let ((value (if (integerp source) source (get state source))))
	(updated state dest value)))

(defun jnz (state source offset)
  (let ((value (if (integerp source) source (get state source))))
	(if (= value 0)
		state
	  ;; The -1 is because the PC has been incremented between reading and executing the instruction
	  (updated state 'p (+ (get state 'p) offset -1)))))

(defun read-instruction (state instructions)
  (nth (get state 'p) instructions))

(defun execute-instruction (state instruction)
  (let ((func (car instruction))
		(args (cdr instruction)))
	(apply func state args)))

(defun read-increment-execute (state instructions)
  (let ((size (length instructions)))
    (while (< (get state 'p) size)
      (let* ((instruction (read-instruction state instructions))
             (incremented (inc state 'p)))
        (setq state (execute-instruction incremented instruction)))))
  state)

(defun display-result (state)
  (mapcar (lambda (r) (message "%s = %d" r (get state r))) '(a b c d p)))

(defconst example-input '((cpy 41 a)
						  (inc a)
						  (inc a)
						  (dec a)
						  (jnz a 2)
						  (dec a)))

(defconst puzzle-input '((cpy 1 a)
						 (cpy 1 b)
						 (cpy 26 d)
						 (jnz c 2)
						 (jnz 1 5)
						 (cpy 7 c)
						 (inc d)
						 (dec c)
						 (jnz c -2)
						 (cpy a c)
						 (inc a)
						 (dec b)
						 (jnz b -2)
						 (cpy c b)
						 (dec d)
						 (jnz d -6)
						 (cpy 19 c)
						 (cpy 14 d)
						 (inc a)
						 (dec d)
						 (jnz d -2)
						 (dec c)
						 (jnz c -5)))

(defun solve-example ()
  (display-result
   (read-increment-execute initial-state example-input)))

(defun solve-puzzle-a ()
  (display-result
   (read-increment-execute initial-state puzzle-input)))

(defun solve-puzzle-b ()
  (display-result
   (let ((puzzle-b-state (updated initial-state 'c 1)))
	 (read-increment-execute puzzle-b-state puzzle-input))))

;; (solve-example)    ;; ("a = 42" "b = 0" "c = 0" "d = 0" "p = 6")
;; (solve-puzzle-a)   ;; ("a = 318077" "b = 196418" "c = 0" "d = 0" "p = 23")
;; (solve-puzzle-b)   ;; ("a = 9227731" "b = 5702887" "c = 0" "d = 0" "p = 23")
