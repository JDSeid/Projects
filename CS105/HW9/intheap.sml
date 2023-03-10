structure IntHeap :> PQUEUE where type elem = int =
struct

type elem = int
val compare_elem (a, b) = Int.compare
datatype t = EMPTY
            | LIST of (elem list)
type pqueue = t
val empty = []
val insert (x, EMPTY)    = LIST ([x])
  | insert (x, LIST(xs)) = LIST([x::xs])

val isEmpty (EMPTY)   = true
  | isEmpty (LIST(_)) = false

exception Empty

val deletemin (EMPTY)          = raise Empty
  | deletemin (LIST(x::[]))    = (x, EMPTY)
  | deletemin (LIST(x::y::ys)) = 
    let val findMin (x::[], currMin) =
                if (x < currMin) then
                    x
                else
                    currMin
    
          | findMin ((x::xs), currMin) =
                if (x < currMin) then
                    findMin(y::ys, x)
                else
                    findMin(y::ys, currMin)
    in
        let val min = findMin(x::xs, x)
            val removeMin((x::y::xs), min) = 
                    if(min == y) then
                        x::xs
                    else
                        x :: removeMin(y::xs)
        in
            LIST(removeMin((x::y::ys), min))
        end
    end