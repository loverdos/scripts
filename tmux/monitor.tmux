#start-server
#new-session -s monitor
rename-session monitor
rename-window catchall

# pane 0

# pane 1 - journalctl
split-window -v
#send "dmesg -T --time-format reltime -wxH -l warn,err,crit,alert,emerg" C-m
send "journalctl -p err..alert -f" C-m

# pane 2 - dstat
split-window -v
send "dstat -nmrl --top-cpu --top-io-adv 5" C-m

select-pane -t 0

# pane 3 - df
split-window -h
send "watch -d -n 1 'df -h'" C-m

select-pane -t 0

# pane 0 - glances
send "glances" C-m
