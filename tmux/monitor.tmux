#start-server
#new-session -s monitor
rename-session monitor
rename-window catchall

# pane 0

# pane 1 - dstat
split-window -v
send "dstat -lrvn 10" C-m

# pane 2 - htop
split-window -v
send htop C-m

select-pane -t 0

# pane 3 - df
split-window -h
send "watch -d -n 1 'df -h'" C-m

select-pane -t 0

# pane 0 - dmesg
send "dmesg -wxH" C-m
