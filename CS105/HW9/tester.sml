open GNatural

val () =
    Unit.checkExpectWith (Unit.order) 
    "compare test"
    (fn () => compare (ofInt 1, ofInt 2))
    GREATER