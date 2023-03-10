;; step 5

;; checking literal int type
(check-type 3 int)
;; checking literal bool type
(check-type #t bool)
;; checking literal sym type
(check-type 'hello sym)



;; step 6

;; checking if w/ int
(check-type (if #f 1 0) int)
;; checking if w/ bool
(check-type (if #f #f #t) bool)
;; checking if w/ error where e1 is not a bool
(check-type-error (if 5 #f #t))
;; checking if w/ error where e2 and e3 do not have same type
(check-type-error (if #f #f 5))


;; step 7

;; checking var with car
(check-type car (forall ['a] ((list 'a) -> 'a)))
;; checking var with null?
(check-type null? (forall ['a] ((list 'a) -> bool)))
;; checking var with cons
(check-type cons (forall ['a] ('a (list 'a) -> (list 'a))))
;; checking var w/ error where var (hello) is undefined.
(check-type-error hello)


;; step 9

;; checking val where it is an int
(val x 5)
(check-type x int)

;; checking val where it is a bool
(val y #t)
(check-type y bool)

;; checking val w/ error where val (z) is undefined
(check-type-error z)


;; step 10

;; checking apply where it is an int
(check-type (+ 0 1) int)
;; checking apply where it is a bool
(check-type (> 0 1) bool)
;; checking apply w/ error where function does not receive correct param types
(check-type-error (+ #f 1))
;;checking apply w/ error where function does not receive correct num of params
(check-type-error (mod 3))

;; step 11

;; checking simple let where it is an int
(check-type (let ((a 5)) a) int)
;; checking simple let where it is a bool
(check-type (let ((b #f)) b) bool)
;; checking more complex let where it is an int
(check-type (let ((a 6) (b 5)) (+ a b)) int)
;; checking let w/ error where it uses undefined val in body
(check-type-error (let ((a 5)) b))
;; checking let w/ error where its body performs invalid operation
(check-type-error (let ((a 6) (b #f)) (+ a b)))

;; step 12

;; checking lambda where it is an int -> int
(check-type (lambda ([y : int]) y) (int -> int))
;; checking lambda where it is an int int -> int
(check-type (lambda ([y : int] [x : int]) (+ x y)) (int int -> int))
;; checking lambda where it is an int int -> bool
(check-type (lambda ([y : int] [x : int]) (> x y)) (int int -> bool))
;; checking lambda w/ error where it performs operation on wrong type in body
(check-type-error (lambda ([x : int] [y : bool]) (+ x y)))
;; checking lambda w/ error where undefined val is used in the body
(check-type-error (lambda ([y : int] [x : int]) (> z y)))


;; step 13

;; checking while
(check-type (while #t 5) unit)
;; checking while w/ error where the second parameter is undefined
(check-type-error (while #t j))
;; checking while w/ error where the first parameter is not a bool
(check-type-error (while 5 5))

;; checking begin where it is a bool
(check-type (begin 5 10 #f) bool)
;; checking begin where it is an int
(check-type (begin 5 #f 1) int)
;; checking begin w/ error where there is an undefined val in the body
(check-type-error (begin j #f 1))

;; checking set where it is an int
(val aSet 1)
(check-type (set aSet 4) int)
;; checking set where it is a bool
(val bSet #f)
(check-type (set bSet #t) bool)

;; checking set w/ error where previously defined val is being set to a new
;; value of a different type.
(check-type-error (set aSet #t))
;; checking set w/ error where it tries to set to a new value to a val that was
;; never defined/initialized
(check-type-error (set xSet 2))

;; step 14

;; checking more complex let* where it is an int
(check-type (let* ([d 10] [e 5]) (+ d e)) int)
;; checking simple let* where it is a bool
(check-type (let* () #t) bool)
;; checking more complex let* where it is a bool
(check-type (let* ([d 10] [e (+ d 1)]) (> d e)) bool)
;; checking let* w/ error where it performs operation on wrong type in body
(check-type-error (let* ([d 10] [e (> d 1)]) (+ d e)))
;; checking let* w/ error where it performs operation on undefined val in body
(check-type-error (let* ([d 10] [e (> d 1)]) (+ w e)))
;; checking let* w/ error where expression of a param contains undefined val
(check-type-error (let* ([d 10] [e (> h 1)]) (+ w e)))

;; step 15

;; checking letrec where it is an int
(check-type (letrec [([x : (int -> int)] (lambda ([y : int]) (+ y 5)))] (x 5))
                                                                            int)
;; checking letrec where it is a bool
(check-type (letrec [([x : (int -> bool)] (lambda ([y : int]) (< y 5)))] (x 5))
                                                                           bool)
;; checking letrec w/ error where it returns type different than specified
(check-type-error (letrec [([x : (int -> int)] (lambda ([y : int]) (< y 5)))]
                                                                         (x 5)))
;; checking letrec w/ error where the param is not of type lambda
(check-type-error (letrec [([x : int] 5)] x))
;; checking letrec w/ error where the lambda performs invalid operation on type
(check-type-error (letrec [([x : (int -> int)] (lambda ([y : int]) (if y 5 4)))]
                                                                         (x 5)))
;; checking letrec w/ error where the lambda is given a different type as param
;; than was specified in definition
(check-type-error (letrec [([x : (bool -> int)] (lambda ([y : int]) (< y 5)))]
                                                                         (x 5)))



;; step 16

;; checking valrec where it is an int -> int
(val-rec [valrec-x : (int -> int)] (lambda ([x : int]) (+ x 1)))
(check-type valrec-x (int -> int))
;; checking valrec where it is an int -> bool
(val-rec [valrec-y : (int -> bool)] (lambda ([x : int]) (< x 1)))
(check-type valrec-y (int -> bool))
;; checking valrec w/ error where the expression in param is not a lambda
(check-type-error (val-rec [valrec-z : int] 5))
;; checking valrec w/ error where the lambda does not return the type specified
(check-type-error (val-rec [valrec-d : (int -> bool)] (lambda ([x : int]) x)))
;; checking valrex w/ error where the lambda is given a different type as param
;; than was specified in definition
(check-type-error (val-rec [valrec-f : (bool -> int)] (lambda ([x : int]) x)))

;; checking define where it is an int -> int
(define int addOne ([x : int]) (+ x 1))
(check-type addOne (int -> int))
;; checking define where it is an int -> bool
(define bool isDoubleDigit ([x : int]) (> x 9))
(check-type isDoubleDigit (int -> bool))
;; checking define w/ error where it performs operation on invalid type in body
(check-type-error (define int errorFun1 ([x : int]) (if x 2 3)))
;; checking define w/ error where it returns a different type than specified
(check-type-error (define bool errorFun2 ([x : int]) x))



;; Step 17

;; checking tyapply where it is an int int -> bool
(check-type [@ = int] (int int -> bool))
;; checking tyapply where it is an (list bool) -> bool
(check-type [@ car bool] ((list bool) -> bool))
;; checking tyapply being utilized in exp that evaluates to bool
(check-type ([@ = int] 5 5) bool)
;; checking tyapply w/ error where tyapply var is instantiated for one type and
;; is given another TypeError
(check-type-error ([@ = bool] 5 5))
;; checking tyapply w/ error where it is applied to non polymorphic var
(check-type-error [@ > bool])

;; checking tylambda where it is forall ['a] bool
(check-type (type-lambda ['a] #t) (forall ['a] bool))
;; checking tylambda where it is forall ['a] (int (list 'a) -> (list 'a)
(val test-func (type-lambda ['a]
              (letrec [ ( [test-func-recursive : (int (list 'a) -> (list 'a)) ]
                  (lambda ([n : int] [xs : (list 'a)])
                    (if ([@ = int] n 0)
                        xs
                        (test-func-recursive (- n 1) xs))))]
              test-func-recursive)))
(check-type test-func (forall ['a] (int (list 'a) -> (list 'a))))
;; checking tylambda w/ error where the polymorphic bariables used in the letrec
;; are not defined in the tylambda
(check-type-error (type-lambda ['b]
                    (letrec [([test-func-error-recursive : (int (list 'a) ->
                                                                     (list 'a))]
                        (lambda ([n : int] [xs : (list 'a)])
                            (if ([@ = int] n 0)
                                xs
                                (test-func-error-recursive (- n 1) xs))))]
                        test-func-error-recursive)))


;; Step 18

;; checking nil list literal where it is a list bool
(check-type [@ '() bool] (list bool))
;; checking Pair(v1, NIL) where it is a list int
(check-type ([@ cons int] 1 [@ '() int]) (list int))
;; checking Pair(v1, v2) where it is a list int
(check-type ([@ cons int] 1 '(4 2 3)) (list int))
;; checking Pair(v1, v2) w/ error where v1 is not the same type as the elements
;; in v2 (i.e. bool going into list int)
(check-type-error ([@ cons int] #f '(4 2 3)))
;; checking Pair(v1, v2) w/ error where cons is instantiated to different type
;; than the types of v1 and v2
(check-type-error ([@ cons int] #f '(#t #f #t)))
;; checking Pair(v1, nil) where v1 is not the same type as the empty list v2
(check-type-error ([@ cons int] #f [@ '() int]))
