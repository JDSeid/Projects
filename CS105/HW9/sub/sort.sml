functor PQSortFn(structure Q : PQUEUE) :> SORT where type elem = Q.elem =
    struct
      type elem = Q.elem
      fun compare (e1, e2) = Q.compare_elem(e1, e2)
      fun sort [] = []
        | sort xs = 
            let val pQueue = List.foldl Q.insert Q.empty xs
                fun sortedList queue =
                    if Q.isEmpty queue then 
                        []
                    else
                        let val (currMin, newQueue) = Q.deletemin queue


                        in
                            currMin :: (sortedList newQueue)
                        end

            in
                sortedList pQueue

            end
end