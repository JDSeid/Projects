fun sum [] = 0
|   sum (x::xs) = x + sum xs

datatype sx3 = STRING of string
            |  NUMBER of int
            |  BOOLEAN of bool
            |  LIST of sx3 list




val () =
  Unit.checkExpectWith Unit.intString "(4)"
  (fn () => foldl (op +) 0 [1, 2, 3])
  7
