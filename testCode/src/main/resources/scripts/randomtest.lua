require 'rubygems'
require 'redis'

r=Redis.new

RandomPushScript = <<EOF
    local i = tonumber(ARGV[1])
    local res
    math.randomseed(tonumber(ARGV[2]))
    while (i>0) do
        res = redis.call('lpush',KEYS[1],math.random())
        i=i-1
    end
    return res
EOF

r.del(:mylist)
puts r.eval(RandomPushScript,[:mylist],[10,rand(2**32)])