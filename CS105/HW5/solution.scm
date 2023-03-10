;;;;;;;;;;;;;;;;;;; COMP 105 SCHEME 3 ASSIGNMENT ;;;;;;;;;;;;;;;

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;;  Exercise 2

;;(list-of? A? v) returns true if v is a list of values each of which satisfies
;;                A?. Returns false otherwise

;;LAWS:

;; (list-of? A? '()) == #t
;; (list-of A? (cons v vs)) == (and (A? v) (list-of? vs))
;; (list-of A? s) == #f, where s is a symbol
;; (list-of A? n) == #f, where s is a number
;; (list-of A? b) == #f, where b is a Boolean
;; (list-of A? f) == #f, where f is a function
;; (list-of A? v) == A? v, where v takes none of the forms above

(define list-of? (A? v)
    (if (null? v)
        #t
        (if (or (function? v) (or (symbol? v) (or (number? v) (boolean? v))))
            #f
            (if (function? v)
                    #f
                    (if (symbol? v)
                            #f
                            (if (number? v)
                                    #f
                                    (if (boolean? v)
                                            #f
                                            (if (pair? v)
                                                (and (A? (car v))
                                                        (list-of? A? (cdr v)))
                                                (A? v)))))))))

        ;;function for Testing
        (define even? (n) (= 0 (mod n 2)))

        (check-assert (list-of? even? '(2 4 6 10)))
        (check-assert (not (list-of? even? '(2 5 6 10))))
        (check-assert (not (list-of? even? 2)))
        (check-assert (list-of? boolean? '(#f #t #t)))
        (check-assert (not (list-of? boolean? '(1 #t #t))))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;;  Exercise 3

;;(formula? x) Given an arbitrary μScheme value, returns #t if the value
;; represents a Boolean formula and #f otherwise.

;;LAWS

;;(formula? x) == #t, where x is a symbol
;;(formula? (make-not f)) == #t, where f is a formula
;;(formula? (make-or fs)) == #t, where fs is a list of formulas
;;(formula? (make-and fs)) == #t, where fs is a list of formulas
;;(formula? v) == #f, where v has none of the above forms


(record not [arg])
(record or  [args])
(record and [args])


(define formula? (x)
    (if (symbol? x)
        #t
        (if (not? x)
            (if (atom? (not-arg x))
                (formula? (not-arg x))
                #f)
            (if (or? x)
                (if (pair? (or-args x))
                    (list-of? formula? (or-args x))
                    #f)
                (if (and? x)
                    (list-of? formula? (and-args x))
                    #f)))))


        (check-assert (formula? 'x))
        (check-assert (formula? (make-not 'x)))
        (check-assert (formula? (make-or '(x y))))
        (check-assert (formula? (make-and '(x y))))
        (check-assert (not (formula? (make-and 'x))))
        (check-assert (not (formula? (make-or 'x))))
        (check-assert (not (formula? (make-not '(x y)))))
        (check-assert (not (formula? 100)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;;  Exercise 4

;;(eval-formula f env) Takes a formula f and an environment env. Returns true
;; iff the formula f is satisfied in the given environment env. Every variable
;; in f must be bound in env

;;LAWS

;; (eval-formula x env) = (find x env), where x is a symbol
;; (eval-formula (make-not f) env) = (not (eval-formula f env))
;; (eval-formula (make-or (cons f fs)) env) = (or (eval-formula f env)
;;                                       (eval-formula (make-not fs) env))
;; (eval-formula (make-and (cons f fs)) env) = (and (eval-formula f env)
;;                                        (eval-formula fs env))
;;;; (eval-formula v env) = #f, where v has none of the above forms


(define eval-formula (f env)
    (if (symbol? f)
        (find f env)
        (if (not? f)
            (not (eval-formula (not-arg f) env))
            (if (or? f)
                (exists? (lambda (y) (eval-formula y env)) (or-args f))
                (if (and? f)
                    (all? (lambda (y) (eval-formula y env)) (and-args f))
                    #f)))))

(check-assert (eval-formula 'x '((x #t))))
(check-assert (not (eval-formula 'x '((x #f)))))
(check-assert (not (eval-formula (make-not 'x) '((x #t)))))
(check-assert (eval-formula (make-not 'x) '((x #f))))
(check-assert (not (eval-formula (make-and '(x y z))
                            (list2 (list2 'x #f) (list2 'y #t)))))
(check-assert (eval-formula (make-and (list3 'x 'y 'z))
                             (list3 (list2 'x #t) (list2 'y #t) (list2 'z #t))))
(check-assert (not (eval-formula (make-and (list3 'x 'y 'z))
                            (list3 (list2 'x #t) (list2 'y #t) (list2 'z #f)))))
(check-assert (eval-formula (make-or '(x y z))
                            (list3 (list2 'x #t) (list2 'y #t) (list2 'z #t))))
(check-assert (eval-formula (make-or '(x y z))
                            (list3 (list2 'x #t) (list2 'y #t) (list2 'z #f))))
(check-assert (not (eval-formula (make-or '(x y z))
                            (list3 (list2 'x #f) (list2 'y #f) (list2 'z #f)))))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;;  Exercise 6


;;(find-formula-true-asst f fail succ) Takes a formula, a failure continuation,
;; and a success continuation. Searches for an assignment that satisfies
;; formula f. If it finds a satisfying assignment, it calls succ, passing both
;; the satisfying assignment (as an association list) and a resume continuation.
;; If it fails to find a satisfying assignment, it calls fail.

;;(f-f-sym x bool cur fail succeed) Checks if x is already defined
;; In the environment. If it is bound to false, it fails. If it bound to true,
;; it succeeds. If it is unbound, it binds it and then succeeds.

;;f-f-sym Laws:
;;(f-f-sym x bool cur fail succeed) == succeed (bind x bool cur) fail),
;;                                             where x is not bound in cur
;;(f-f-sym x bool cur fail succeed) == (succeed cur fail),
;;                                             where x is bool in cur
;;(f-f-sym x bool cur fail succeed) == (succeed cur fail),
;;                                             where x is (not bool) in cur


;; (f-all-a formulas bool cur fail succeed) extends cur to find an
;; assignment that makes every formula in the list formulas equal to bool.


;;f-all-a Laws:
;;(f-all-a '()         bool cur fail succeed) == (succeed cur fail)
;;(f-all-a (cons f fs) bool cur fail succeed) == (f-f-a f bool cur fail
;;                                     (lambda (cur resume)
;;                                       (f-all-a fs bool cur resume succeed)))


;;(f-any-a formulas bool cur fail succeed) extends cur to find an assignment
;; that makes any one of the formulas equal to bool.

;;f-any-a Laws:
;;(f-any-a '()         bool cur fail succeed) == (fail)
;;(f-any-a (cons f fs) bool cur fail succeed) == (f-f-a f bool cur
;;                                       (lambda ()
;;                                          (f-any-a fs bool cur fail succeed))
;;                                       succeed)

;;(f-f-a formula bool cur fail succeed) extends assignment cur to find an
;; assignment that makes the single formula equal to bool.

;; f-f-a Laws:
;;(f-f-a x bool cur fail succeed) == (f-f-sym x bool cur fail succeed),
;;                                                        where x is a symbol
;;(f-f-a (make-not f)  bool cur fail succeed) == (f-f-a f (not bool)
;;                                                           cur fail succeed)
;;(f-f-a (make-or  fs) #t   cur fail succeed) == (f-any-a f #t cur fail succeed)
;;(f-f-a (make-or  fs) #f   cur fail succeed) == (f-all-a f #f cur fail succeed)
;;(f-f-a (make-and fs) #t   cur fail succeed) == (f-all-a f #t cur fail succeed)
;;(f-f-a (make-and fs) #f   cur fail succeed) == (f-any-a f #f cur fail succeed)


(define find-formula-true-asst (f fail succ)
    (letrec(
            (f-f-sym (lambda (x bool cur fail succeed)
                        (if (null? (find x cur))
                            (succeed (bind x bool cur) fail)
                            (if (equal? (find x cur) bool)
                                (succeed cur fail)
                                (fail)))))

            (f-all-a  (lambda (formulas bool cur fail succeed)
                        (if (null? formulas)
                            (succeed cur fail)
                            (f-f-a (car formulas) bool cur fail
                                (lambda (cur resume)
                                    (f-all-a (cdr formulas)
                                        bool cur resume succeed))))))
            (f-any-a  (lambda (formulas bool cur fail succeed)
                        (if (null? formulas)
                            (fail)
                            (f-f-a (car formulas) bool cur
                                (lambda ()
                                    (f-any-a (cdr formulas)
                                        bool cur fail succeed)) succeed))))
            (f-f-a    (lambda (formula bool cur fail succeed)
                        (if (symbol? formula)
                            (f-f-sym formula bool cur fail succeed)
                            (if (not? formula)
                                (f-f-a (not-arg formula)
                                    (not bool) cur fail succeed)
                                (if (or? formula)
                                    (if (equal? #t bool)
                                        (f-any-a (or-args formula)
                                            bool cur fail succeed)
                                        (f-all-a (or-args formula)
                                            bool cur fail succeed))
                                    (if (and? formula)
                                        (if (equal? #t bool)
                                            (f-all-a (and-args formula)
                                                bool cur fail succeed)
                                            (f-any-a (and-args formula)
                                                bool cur fail succeed))
                                        (fail))))))))
         (f-f-a f #t '() fail succ)))





;;(use solver_tests.scm)
;;(use solver-interface-tests.scm)
