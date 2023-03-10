(* Starter code for Exercise 1 of Modules assignement.
*  Authors: Kell Pogue and Norman Ramsey (edits by Richard Townsend) *) 

structure GNatural = struct
  datatype nat = ZERO
               | TIMESBASEPLUS of nat * int

  val base = 0x8000

  (* abstraction function
     A ZERO = 0
     A (TIMESBASEPLUS m, d) = base * (A m) + d
  *)

  (* Invariants:
  *   In value of the form TIMESBASEPLUS (m, d),
  *     - m and d are not both zero
  *     - d is a machine integer in the range 0 <= d < base
  *)
  fun invariant ZERO = true
    | invariant (TIMESBASEPLUS (ZERO, 0)) = false
    | invariant (TIMESBASEPLUS (n, d)) = d < base andalso d >= 0
                                         andalso invariant n

  (* constructs nat values that preserve first invariant *)
  fun timesBasePlus (ZERO, 0) = ZERO
    | timesBasePlus (n, d) = TIMESBASEPLUS (n, d)

  exception Negative    (* raised if any operation's result would be negative *)

  exception BadDivisor

  (* converts a nonnegative machine integer into a natural number *)
  fun ofInt 0 = ZERO
    | ofInt n = timesBasePlus (ofInt (n div base), n mod base)

  infix 6 /+/ /-/
  infix 7 /*/ sdiv


  (* carryIntoNat (n, c) adds natural number n to carry bit c. A carry bit is a
  *  machine integer that is either 0 or 1. It raises exception Match if
  *  provided an invalid carry bit.
  *)
  fun carryIntoNat (n, 0) = n
    | carryIntoNat (ZERO, 1) = timesBasePlus (ZERO, 1)
    | carryIntoNat (TIMESBASEPLUS (m, d), 1) =
        timesBasePlus (carryIntoNat (m, (d + 1) div base), (d + 1) mod base)
    | carryIntoNat _  = raise Match (* bad carry bit *)

  (* addWithCarry (n1, n2, c) takes natural numbers n1 and n2 and carry bit c,
  *  and returns n1 + n2 + c.
  *)
  fun addWithCarry (x, ZERO, c) = carryIntoNat (x, c)
    | addWithCarry (ZERO, y, c) = carryIntoNat (y, c)
    | addWithCarry (TIMESBASEPLUS (m1, d1), TIMESBASEPLUS (m2, d2), c) =
        let val d  = (d1 + d2 + c) mod base
            val c' = (d1 + d2 + c) div base (* the "carry out" *)
        in  timesBasePlus (addWithCarry (m1, m2, c'), d)
        end

  (* Addition on two natural numbers x and y *)
  fun x /+/ y = addWithCarry (x, y, 0)

  (* borrowFromNat (n, b) takes a natural number n and a borrow bit b and
  * returns n - b, provided that that difference is a natural number. If not,
  * the function raises exception Negative. It raises exception Match given an
  * invalid borrow bit.
  *)
  fun borrowFromNat (n, 0) = n
    | borrowFromNat (TIMESBASEPLUS (m, 0), 1) =
        timesBasePlus (borrowFromNat (m, 1), 9)
    | borrowFromNat (TIMESBASEPLUS (m, d), 1) =
        timesBasePlus (m, d - 1)
    | borrowFromNat (ZERO, 1) = raise Negative
    | borrowFromNat _         = raise Match (* bad carry bit *)

  (* Given natural numbers n1 and n2 and borrow bit b, return n1 - n2 - b if
  *  that difference is a natural number. Otherwise raise exception Negative.
  *)
  fun subWithBorrow (x, ZERO, b) = borrowFromNat (x, b)
    | subWithBorrow (TIMESBASEPLUS (m1, d1), TIMESBASEPLUS (m2, d2), b) =
        let val d = (d1 - d2 - b) mod base
            val b' = if d1 - d2 - b < 0 then 1 else 0
        in  timesBasePlus (subWithBorrow (m1, m2, b'), d)
        end
    | subWithBorrow (ZERO, TIMESBASEPLUS _, b) = raise Negative

  (* Subtraction on two natural numbers x and y *)
  fun x /-/ y = subWithBorrow (x, y, 0)

  (* Helper function to clean up construction of natural numbers of the for
  *   x * base + 0
  *)
  fun timesBase x = timesBasePlus (x, 0)

  (* Multiplication on two natural numbers *)
  fun ZERO /*/ _     = ZERO
    | _    /*/ ZERO  = ZERO
    | (TIMESBASEPLUS (m1, d1)) /*/ (TIMESBASEPLUS (m2, d2)) =
              ofInt (d1 * d2)
          /+/ timesBase (m1 /*/ ofInt d2)
          /+/ timesBase (m2 /*/ ofInt d1)
          /+/ timesBase (timesBase (m1 /*/ m2))

  exception LeftAsExercise



  
  (* sdiv (n, d) divides natural number n by digit d using short division. 
     The return value is a record of the form
         
       { quotient = q, remainder = r}

     where q and r satisfy these laws:

       n == q /*/ ofInt d /+/ ofInt r
       0 <= r < d

     If d <= 0 or d > base (where "base" is the hidden base selected to
     implement natural numbers), sdiv (n, d) raises BadDivisor.
  *)
 fun  _ sdiv 0 = raise BadDivisor
    | ZERO sdiv _ = { quotient = ZERO, remainder = 0 }
    | (TIMESBASEPLUS (m, d)) sdiv v =
        let 
            val { quotient = q', remainder = r'} = m sdiv v
            val q = timesBase q' /+/ ofInt ((r' * base + d) div v)
            val r = (r' * base + d) mod v
        in
            if (d <= 0x0) orelse (d > base) then 
              raise BadDivisor 
            else
              { quotient = q, remainder = r } 
        end
      


(* Compare two natural numbers, following these hand-wavy laws:
  *
  *  compare (n1, n2) == EQUAL, when n1 == n2
  *  compare (n1, n2) == LESS, when n1 < n2
  *  compare (n1, n2) == GREATER, when n1 > n2
  *
  *)
  fun compare (x, y) = 
    let val diff = x /-/ y
    in
        case diff of ZERO => EQUAL
                    | _    => GREATER
    end handle Negative => LESS







  (* decimal n returns a list giving the natural decimal
     representation of n, most significant digit first.
     For example,  decimal (ofInt 123) = [1, 2, 3]
                   decimal (ofInt 0)   = [0]
     It must never return an empty list.
     And when it returns a list of two or more digits,
     the first digit must not be zero.
  *)
  fun decimal ZERO = [0]
    | decimal x    = 

          let fun decimal_helper ZERO = []
            | decimal_helper x'       = 
                let val { quotient = q, remainder = r } = x' sdiv 10
                in
                  List.concat [decimal_helper q, [r]]
                end
          in
              decimal_helper x
          end
end

structure Natural :> NATURAL = GNatural
