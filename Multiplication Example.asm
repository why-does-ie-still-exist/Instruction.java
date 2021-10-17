mov ab, 0d
mov bb, 5d
mov cb, 7d
start:
add cb, 0d
jez outside
add ab, bb
sub cb, 1d
jmp start
outside:
jmp outside