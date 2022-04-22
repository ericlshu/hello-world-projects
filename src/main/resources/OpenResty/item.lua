-- 导入common函数库
local common = require('common')
local read_http = common.read_http
local read_redis = common.read_redis
-- 导入cjson库
local cjson = require "cjson"
-- 到入共享词典，本地缓存
local item_cache = ngx.shared.item_cache

-- 封装查询函数
function read_date(key, expire, path, params)
    -- 查询本地缓存
    local resp = item_cache:get(key);
    if not resp then
        ngx.log(ngx.ERR, '本地缓存查询失败，尝试查询Redis， key : ', key)
        -- 查询redis
        resp = read_redis('127.0.0.1', 6379 , key)
        if not resp then
            ngx.log(ngx.ERR, 'Redis缓存查询失败，尝试查询http, key : ', key)
            resp = read_http(path,params)
        end
    end
    -- 查询成功，把数据写入本地结果
    item_cache:set(key,resp,expire)
    return resp
end

-- 获取路径参数
local id = ngx.var[1]
-- 查询商品信息
local itemJSON = read_date("item:id:" .. id, 1800, "/item/" .. id, nil)
-- 查询库存信息
local stockJSON = read_date("item:stock:id:" .. id, 60, "/item/stock/" .. id, nil)
-- JSON转化为lua的table
local item = cjson.decode(itemJSON)
local stock = cjson.decode(stockJSON)
-- 组合数据
item.stock = stock.stock
item.sold = stock.sold
-- 序列化json返回结果
ngx.say(cjson.encode(item))

