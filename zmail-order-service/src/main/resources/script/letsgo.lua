local key = KEYS[1]
local arg = ARGV[1]
local trueValue = redis.call('get', key)
if trueValue == arg then
    return 0
else
    return 8
end
