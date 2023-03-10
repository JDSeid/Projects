;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;
;;;;  Exercise 2



(val drop (type-lambda ['a]
              (letrec [ ( [drop-recursive : (int (list 'a) -> (list 'a)) ]
                  (lambda ([n : int] [xs : (list 'a)])
                    (if ([@ = int] n 0)
                        xs
                    (if ([@ null? 'a] xs)
                        [@ '() 'a]
                        (drop-recursive (- n 1) ([@ cdr 'a] xs))
                    )
                  )
                  )
              )
              ]
              drop-recursive
          )
)
)

(check-type drop (forall ['a] (int (list 'a) -> (list 'a))))
(check-expect ([@ drop int] 2 '(1 2 3)) '(3))
(check-expect ([@ drop int] 5 [@ '() int]) [@ '() int])
(check-expect ([@ drop sym] 2 ([@ list3 sym] 'hello 'hey 'howdy))
                                                        ([@ list1 sym] 'howdy))
(check-expect ([@ drop bool] 1 ([@ list3 bool] #t #t #f))
                                                        ([@ list2 bool] #t #f))




(val takewhile (type-lambda ['a]
              (letrec [ ( [takewhile-recursive : (('a -> bool) (list 'a) ->
                                                                    (list 'a))]
                  (lambda ([p? : ('a -> bool)] [xs : (list 'a)])

                    (if ([@ null? 'a] xs)
                        [@ '() 'a]
                        (if (p? ([@ car 'a] xs))
                            (takewhile-recursive p? ([@ cdr 'a] xs))
                            xs
                        )
                    )
                  )
              )
              ]
              takewhile-recursive
          )
)
)



;;Testing function
(define bool even? ([x : int]) ([@ = int] (mod x 2) 0))
(define bool is-hi? ([x : sym]) ([@ = sym] x 'hi))
(check-type takewhile (forall ['a] (('a -> bool) (list 'a) -> (list 'a))))
(check-expect ([@ takewhile int] even? '(2 4 6 7 8 10 12)) '(7 8 10 12))
(check-expect ([@ takewhile sym] is-hi? ([@ list3 sym] 'hi  'hello 'howdy))
                                                ([@ list2 sym] 'hello 'howdy))
(check-expect ([@ takewhile int] even? '(2 4 6 8 10 12)) [@ '() int])
