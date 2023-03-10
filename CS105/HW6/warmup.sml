(***** Problem 1 *****)

(* Takes in a list. Returns true if the list is empty, and false otherwise *)
fun mynull [] = true
  | mynull _ = false



(* Unit Test *)
      val () =
          Unit.checkAssert "[] is null"
          (fn () => mynull [])
      val () =
          Unit.checkAssert "[1, 2] is nonnull"
          (fn () => not (mynull [1, 2]))


(***** Problem 2 *****)

(* Takes in a list xs. Returns the list reversed *)
fun reverse xs = foldl (op ::) [] xs



(* (int list) string buffer for unit testing *)
val int_list_toString = Unit.listString Unit.intString
(* Unit Test *)
        val () =
          Unit.checkExpectWith int_list_toString "reversing empty"
          (fn () => reverse [])
          []
        val () =
            Unit.checkExpectWith int_list_toString "reversing [1,2,3]"
            (fn () => reverse [1, 2, 3])
            [3, 2, 1]


val () = Unit.reportWhenFailures ()  (* put me at the _end_ *)

(* Takes in a list of integers. Returns the smallest integer. Raises an
exception if the list is empty *)

fun minlist [] = raise Empty
  | minlist (x::xs) = foldl Int.min x (x::xs)


  (* Unit Test *)
      val () =
          Unit.checkExpectWith Unit.intString "smallest of singleton"
          (fn () => minlist [1])
          1

      val () =
          Unit.checkExpectWith Unit.intString "smallest of list with negatives"
          (fn () => minlist [1, 2, ~1, ~5])
          ~5

      val () =
          Unit.checkExnWith Unit.intString
          "Empty List"
          (fn () => minlist [])


(***** Problem 3 *****)

exception Mismatch

(* Takes in two lists, xs and ys. Returns a list of pairs containing elements
 in both lists. Throws an exception if xs and ys are of different lengths *)
fun zip ((x::xs), (y::ys)) = (x, y) :: zip (xs, ys)
  | zip ([], [])           = []
  | zip (_, _)                = raise Mismatch



(* Some new string builders *)
val int_pair_toString = Unit.pairString Unit.intString Unit.intString
val list_pair_toString = Unit.listString int_pair_toString
(* Unit Test *)
        val () =
          Unit.checkExpectWith list_pair_toString "zip on integer lists"
          (fn () => zip ([1, ~2, 4], [9, 12, 0]))
          [(1, 9), (~2, 12), (4, 0)]

        val () =
          Unit.checkExnWith list_pair_toString "zip on uneven lists"
          (fn () => zip ([1, ~2, 4], [9, 12]))



(***** Problem 4 *****)

(* Applies a three-argument function to a pair of lists of equal length,
moving from right to left. Raises an exception if the lists are of unequal
length *)
fun pairfoldrEq f accum ([], [])        = accum
  | pairfoldrEq f accum ((x::xs), (y::ys)) = f (x, y,
                                                pairfoldrEq f accum (xs, ys))
  | pairfoldrEq _ _ _ = raise Mismatch


(* Unit Test *)
        val () =
          Unit.checkExpectWith Unit.intString "pairfoldrEq"
          (fn () => pairfoldrEq (fn (x, y, z) => x + y + z) 0
                                                        ([1, 2, 3],[4, 5, 6]))
          21

          (* Unit Test *)
        val () =
          Unit.checkExpectWith Unit.intString "pairfoldrEq"
          (fn () => pairfoldrEq (fn (x, y, z) => x + y - z) 0
                                                        ([1, 2, 3],[4, 5, 6]))
          7

(* Takes in two lists, xs and ys. Returns a list of pairs containing elements
 in both lists. Throws an exception if xs and ys are of different lengths *)
fun ziptoo ([], []) = []
  | ziptoo ((x::xs), (y::ys)) =
            pairfoldrEq (fn (a, b, c) => (a, b)::c) [] (x::xs, y::ys)
  | ziptoo  (_, _)  = raise Mismatch


(* Unit Test *)
        val () =
          Unit.checkExpectWith list_pair_toString "ziptoo on integer lists"
          (fn () => ziptoo ([1, ~2, 4], [9, 12, 0]))
          [(1, 9), (~2, 12), (4, 0)]

        val () =
          Unit.checkExnWith list_pair_toString "ziptoo on uneven lists"
          (fn () => ziptoo ([1, ~2, 4], [9, 12]))


(***** Problem 5 *****)


(* Takes in a list of lists and combines the elements into a single list *)
fun concat xs = foldl (op @) [] (List.rev xs)



(* Unit Test *)
        val () =
          Unit.checkExpectWith int_list_toString "concat on empty list"
          (fn () => concat [] )
          []

        val () =
          Unit.checkExpectWith int_list_toString "concat on two nonempty lists"
          (fn () => concat [[1, 2], [3, 4]])
          [1, 2, 3, 4]

        val () =
          Unit.checkExpectWith int_list_toString "concat on several lists"
          (fn () => concat [[1, 2], [3, 4], [], [5, 6, 7], [], [8, 9], [10]])
          [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]



(***** Problem 6 *****)

datatype ordsx
  = BOOL of bool
  | NUM  of int
  | SYM  of string
  | SXS  of ordsx list


(* Takes in a list of integers. Returns an S-expression which is a list of the
 S-expression representation of the integers *)
fun numbersSx xs = SXS (map NUM xs)


fun sxString (SYM s)   = s
  | sxString (NUM n)   = Unit.intString n
  | sxString (BOOL b)  = if b then "true" else "false"
  | sxString (SXS sxs) = "(" ^ String.concatWith " " (map sxString sxs) ^ ")"

(* Unit Test *)
        val () =
          Unit.checkExpectWith sxString "numberSx with empty list"
          (fn () => numbersSx [] )
          (SXS [])

        val () =
          Unit.checkExpectWith sxString "numberSx with nonempty list"
          (fn () => numbersSx [1, 2, 3] )
          (SXS [NUM 1, NUM 2, NUM 3])




(* Takes in an ordinary s expression. Returns a list of all symbols in the
    S expression  *)

fun flattenSyms (SXS xs) =
    let fun getSymbol (SYM s) = s
          | getSymbol _   = ""
    in
        List.filter (fn (x) => not (x = "")) (List.map getSymbol xs)
    end
  | flattenSyms (SYM s)  = [s]
  | flattenSyms _ = []



  (* Unit Test *)

      val () =
        Unit.checkExpectWith (Unit.listString (fn (s) => s))
        "flattenSyms with empty list"
        (fn () => flattenSyms (SXS []))
        []

      val () =
        Unit.checkExpectWith (Unit.listString (fn (s) => s))
        "flattenSyms with list of strings"
        (fn () => flattenSyms (SXS [SYM "a", SYM "b", SYM "c" ]))
        ["a", "b", "c"]

      val () =
        Unit.checkExpectWith (Unit.listString (fn (s) => s))
         "flattenSyms with list of strings and nonstrings"
        (fn () => flattenSyms (SXS [BOOL true , SYM "a" , SYM "b", NUM 1,
                                                SXS[NUM 2],  SYM "c" ]))
        ["a", "b", "c"]
