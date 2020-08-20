--Redis：SETNX实现分布式锁之确保只能删自己的锁
--这里用LUA脚本保证速度和原子性
if redis.call('get', KEYS[1]) == ARGV[1]
then
    return redis.call('del', KEYS[1])
else
    return 0
end
