---
--- Created by binghe.
---

local number_util = {}
--long类型的数字转字符串，解决如(2.0190531043722e+15)这种数字转字符串的问题
function number_util.LongNumber2String(longNumber)
    local result = ""
    local startAppend = false
    for i = 25, 0, -1 do
        local pow = math.pow(10, i)
        local compare = math.floor(longNumber / pow)
        --print(compare, number, pow)
        if compare == 0 then
            if startAppend then
                result = result .. "0"
            end
        else
            startAppend = true
            result = result .. compare
            longNumber = longNumber - pow * compare
        end
    end
    return result
end

return number_util
