; Template for SAT Solver Test Cases

(record not [arg])   ;; OK if these are duplicates
(record or  [args])
(record and [args])


;; x and y and z
(val f1 (make-and (list3 'x 'y 'z)) )
(val s1  '((x #t) (y #t) (z #t)))

;; (x and y and z) and (not (x or y or z))
(val f2 (make-and (list2 (make-and (list3 'x 'y 'z))
                                    (make-not (make-or (list3 'x 'y 'z))))))
(val s2 'no-solution)

;; x and (y or z) and (not y)
(val f3 (make-and (list3 'x (make-or (list2 'y 'z)) (make-not 'y))))
(val s3 '((x #t) (y #f) (z #t)))
