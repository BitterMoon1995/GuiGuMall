--Redis：SETNX实现分布式锁之确保只能删自己的锁
--这里用LUA脚本保证速度和原子性

--get(KEYS[1])：该SKU的唯一互斥锁锁
--ARGV[1]：待校验的锁，可能是其他线程的，这里强调其他线程没资格删本线程的锁
--小妙招：Redis-LUA必须要有返回语句，KEYS[]和ARGV[]数组名是固定的，且区分大小写
if redis.call('get', KEYS[1]) == ARGV[1]
then
    return redis.call('del', KEYS[1])
else
    return 0
end
