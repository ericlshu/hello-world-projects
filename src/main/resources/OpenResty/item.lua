-- 导入common函数库
local common = require('common')
local read_http = common.read_http
local read_redis = common.read_redis
-- 导入cjson库
local cjson = require "cjson"

-- 封装查询函数
function read_date(key, path, params)
    local resp = read_redis('127.0.0.1', 6379 , key)
    if not resp then
        ngx.log('Redis查询失败，进行http查询, key : ', key)
        resp = read_http(path,params)
    end
    return resp
end

-- 获取路径参数
local id = ngx.var[1]
-- 查询商品信息
local itemJSON = read_date("item:id:" .. id, "/item/" .. id, nil)
-- 查询库存信息
local stockJSON = read_date("item:stock:id:" .. id, "/item/stock/" .. id, nil)
-- JSON转化为lua的table
local item = cjson.decode(itemJSON)
local stock = cjson.decode(stockJSON)
-- 组合数据
item.stock = stock.stock
item.sold = stock.sold
-- 序列化json返回结果
ngx.say(cjson.encode(item))

