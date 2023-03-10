echo Small Test
/usr/bin/time ./um midmark.um > /dev/null
valgrind --tool=callgrind --dump-instr=yes ./um midmark.um
echo ==========================================================================
echo Medium Test
/usr/bin/time ./um advent.umz < adventure_input.txt > /dev/null
echo ==========================================================================
echo Large Test
/usr/bin/time ./um sandmark.umz > sandmark.txt
diff sandmark.txt sandmark.out