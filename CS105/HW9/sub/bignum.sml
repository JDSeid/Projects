functor BignumFn(structure N : NATURAL) :> BIGNUM
  =
struct
  datatype bigint = NEG of N.nat
                  | NONNEG of N.nat


(* ABSTRACT LAWS
    NEG nat    == nat * -1
    NONNEG nat == nat

*)

    fun invariant (NEG n)    = N.invariant n 
      | invariant (NONNEG n) = N.invariant n

    exception BadDivision

    infix 6 <+> <->
    infix 7 <*> sdiv

    val /+/ = N./+/
    val /-/ = N./-/
    val /*/ = N./*/

    infix 6 /+/ /-/
    infix 7 /*/ 

    fun ofInt n = 
        if n < 0 then
            NEG (N.ofInt (~1 * (n + 1)) /+/ (N.ofInt 1)) 
        else
            NONNEG (N.ofInt n)
    
    fun negated (NEG n)    = NONNEG n
      | negated (NONNEG n) = 
            if N.compare(n, N.ofInt 0) = EQUAL then
                NONNEG n
            else
                NEG n
    
    fun (NONNEG n1) <+> (NONNEG n2) = NONNEG (n1 /+/ n2) 
      | (NEG n1)    <+> (NEG n2)    = NEG (n1 /+/ n2)
      | (NONNEG n1) <+> (NEG n2)    = 
        let val comp = N.compare(n1, n2) in 
            if comp = LESS then
                NEG (n2 /-/ n1)
            else 
                NONNEG (n1 /-/ n2)
        end
      | (NEG n1)    <+> (NONNEG n2) =  (NONNEG n2) <+> (NEG n1)
    

    fun (NONNEG n1) <-> (NONNEG n2) = (NONNEG n1) <+> (NEG n2)
      | (NEG n1)    <-> (NEG n2)    = (NEG n1) <+> (NONNEG n2)
      | (NEG n1)    <-> (NONNEG n2) = NEG (n1 /+/ n2)
      | (NONNEG n1) <-> (NEG n2)    = (NONNEG n1) <+> (NONNEG n2)

    fun (NONNEG n1) <*> (NONNEG n2) = NONNEG (n1 /*/ n2)
      | (NONNEG n1) <*> (NEG n2) = 
        if N.compare(n1, N.ofInt 0) = EQUAL then
            NONNEG (N.ofInt 0)
        else 
            NEG (n1 /*/ n2)
      | (NEG n1) <*> (NEG n2) = NONNEG (n1 /*/ n2)
      | (NEG n1) <*> (NONNEG n2) = 
        if N.compare(n2, N.ofInt 0) = EQUAL then
            NONNEG (N.ofInt 0)
        else 
            NEG (n1 /*/ n2)
    
    fun compare (NONNEG n1, NONNEG n2) = N.compare(n1, n2)
      | compare (NONNEG n1, NEG n2)    = GREATER
      | compare (NEG n1, NONNEG n2)    = LESS
      | compare (NEG n1, NEG n2)       = compare (NONNEG n2, NONNEG n1)
    

    fun toString (NEG n)    = "-" ^ (List.foldr (op ^) ""  (List.map Int.toString (N.decimal n)))
      | toString (NONNEG n) = List.foldr (op ^) ""  (List.map Int.toString (N.decimal n))


    fun (NONNEG n) sdiv 0 = raise BadDivision
      | (NONNEG n) sdiv d =
            let val { quotient = q, remainder = r } = N.sdiv (n, d) in 
                { quotient = NONNEG q, remainder = r } 
                handle N.BadDivisor => raise BadDivision
            end 
      | _ sdiv _ = raise BadDivision
end