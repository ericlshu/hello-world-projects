```
GET _search
{
  "query": {
    "match_all": {}
  }
}

GET /

POST /_analyze
{
  "analyzer": "ik_smart",
  "text": "黑马程序员学习java太棒了！"
}

POST /_analyze
{
  "analyzer": "ik_max_word",
  "text": "黑马程序员学习java太棒了！"
}

POST /_analyze
{
  "analyzer": "pinyin",
  "text": "好好学习，天天向上"
}

PUT /eric
{
  "mappings": {
    "properties": {
      "info": {
        "type": "text",
        "analyzer": "ik_smart"
      },
      "email": {
        "type": "keyword",
        "index": false
      },
      "name": {
        "type": "object",
        "properties": {
          "firstName": {
            "type": "keyword",
            "index": true
          },
          "lastName": {
            "type": "keyword"
          }
        }
      }
    }
  }
}

GET /eric

PUT /eric/_mapping
{
  "properties": {
    "age":{
      "type": "integer"
    }
  }
}

POST /eric/_doc/1
{
    "info": "传智播客Java讲师",
    "email": "zy@itcast.cn",
    "name": {
        "firstName": "云",
        "lastName": "赵"
    }
}

GET /eric/_doc/1

DELETE /eric/_doc/1

POST /eric/_update/1
{
  "doc": {
    "email": "ZhaoYun@itcast.cn"
  }
}

GET /hotel

DELETE /hotel

PUT /hotel
{
  "mappings": {
    "properties": {
      "id": {
        "type": "keyword"
      },
      "name":{
        "type": "text",
        "analyzer": "ik_max_word",
        "copy_to": "all"
      },
      "address":{
        "type": "keyword",
        "index": false
      },
      "price":{
        "type": "integer"
      },
      "score":{
        "type": "integer"
      },
      "brand":{
        "type": "keyword",
        "copy_to": "all"
      },
      "city":{
        "type": "keyword",
        "copy_to": "all"
      },
      "starName":{
        "type": "keyword"
      },
      "business":{
        "type": "keyword"
      },
      "location":{
        "type": "geo_point"
      },
      "pic":{
        "type": "keyword",
        "index": false
      },
      "all":{
        "type": "text",
        "analyzer": "ik_max_word"
      }
    }
  }
}

GET /hotel/_doc/61083

GET /hotel/_search
{
  "query": {
    "match_all": {}
  }
}

# 全文检索查询

GET /hotel/_search
{
  "query": {
    "match": {
      "all": "外滩如家"
    }
  }
}

GET /hotel/_search
{
  "query": {
    "multi_match": {
      "query": "外滩如家",
      "fields": ["brand","name","business"]
    }
  }
}

# term精确查询

GET /hotel/_search
{
  "query": {
    "term": {
      "city": {
        "value": "上海"
      }
    }
  }
}

# range精确查询

GET /hotel/_search
{
  "query": {
    "range": {
      "price": {
        "gte": 1000,
        "lte": 2000
      }
    }
  }
}

# 根据经纬度geo_bounding_box查询

GET /hotel/_search
{
  "query": {
    "geo_bounding_box": {
      "location": {
        "top_left": {
          "lat": 31.1,
          "lon": 121.5
        },
        "bottom_right": {
          "lat": 30.9,
          "lon": 121.7
        }
      }
    }
  }
}

# 根据经纬度geo_distance查询

GET /hotel/_search
{
  "query": {
    "geo_distance": {
      "distance": "5km",
      "location": "31.21,121.5"
    }
  }
}

# Function Score Query
# 过滤条件：哪些文档要加分
# 算分函数：如何计算function score
# 加权方式：function score 与 query score如何运算

GET /hotel/_search
{
  "query": {
    "function_score": {
      "query": {
        "match": {
          "all": "外滩"
        }
      },
      "functions": [
        {
          "filter": {
            "term": {
              "brand": "如家"
            }
          },
          "weight": 10
        }
      ],
      "boost_mode": "sum"
    }
  }
}

# Boolean Query
# must：必须匹配每个子查询，类似“与”
# should：选择性匹配子查询，类似“或”
# must_not：必须不匹配，不参与算分，类似“非”
# filter：必须匹配，不参与算分

GET /hotel/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "term": {
            "city": {
              "value": "上海"
            }
          }
        }
      ],
      "should": [
        {
          "term": {
            "brand": "皇冠假日"
          }
        },
        {
          "term": {
            "brand": "华美达"
          }
        }
      ],
      "must_not": [
        {
          "range": {
            "price": {
              "lte": 500
            }
          }
        }
      ],
      "filter": [
        {
          "range": {
            "score": {
              "gte": 45
            }
          }
        }
      ]
    }
  }
}

GET /hotel/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {"name": "如家"}
        }
      ],
      "must_not": [
        {
          "range": {"price": {"gt": 400}}
        }
      ],
      "filter": [
        {
          "geo_distance": {
            "distance": "10km",
            "location": {"lat": 31.21,"lon": 121.5}
          }
        }
      ]
    }
  }
}

# sort排序

# 对酒店数据按照用户评价降序排序，评价相同的按照价格升序排序

GET /hotel/_search
{
  "query": {"match_all": {}},
  "sort": [
    {"score": "desc"},
    {"price": "asc"}
  ]
}

# 实现对酒店数据按照到你的位置坐标的距离升序排序

GET /hotel/_search
{
  "query": {"match_all": {}},
  "sort": [
    {"_geo_distance": {
      "location": {
        "lat": 31.034661,
        "lon": 121.612282
      },
      "order": "asc",
      "unit": "km"
    }}
  ]
}

# 分页查询

GET /hotel/_search
{
  "query": {"match_all": {}},
  "sort": [{"price": "asc"}],
  "from": 10,
  "size": 20
}

# 搜索结果中把搜索关键字突出显示

GET /hotel/_search
{
  "query": {
    "match": {"all": "如家"}
  },
  "highlight": {
    "fields": {
      "name": {"require_field_match": "false"}
    }
  }
}

POST /hotel/_update/1902197537
{
    "doc": {
        "isAD": true
    }
}
POST /hotel/_update/2056126831
{
    "doc": {
        "isAD": true
    }
}
POST /hotel/_update/1989806195
{
    "doc": {
        "isAD": true
    }
}
POST /hotel/_update/2056105938
{
    "doc": {
        "isAD": true
    }
}

# DSL实现Bucket聚合并排序

GET /hotel/_search
{
  "size": 0,
  "aggs": {
    "brandAgg": {
      "terms": {
        "field": "brand",
        "size": 20,
        "order": {
          "_count": "asc"
        }
      }
    }
  }
}

# 聚合功能，限定聚合范围

GET /hotel/_search
{
  "query": {
    "range": {
      "price": {
        "lte": 200
      }
    }
  }, 
  "size": 0,
  "aggs": {
    "brandAgg": {
      "terms": {
        "field": "brand",
        "size": 20,
        "order": {
          "_count": "asc"
        }
      }
    }
  }
}

# Metrics聚合

GET /hotel/_search
{
  "size": 0,
  "aggs": {
    "brandAgg": {
      "terms": {
        "field": "brand",
        "size": 20,
        "order": {
          "scoreAgg.avg": "desc"
        }
      },
      "aggs": {
        "scoreAgg": {
          "stats": {
            "field": "score"
          }
        }
      }
    }
  }
}

DELETE /test

# 自定义拼音分词器

PUT /test
{
  "settings": {
    "analysis": {
      "analyzer": { 
        "my_analyzer": { 
          "tokenizer": "ik_max_word",
          "filter": "py"
        }
      },
      "filter": {
        "py": { 
          "type": "pinyin",
          "keep_full_pinyin": false,
          "keep_joined_full_pinyin": true,
          "keep_original": true,
          "limit_first_letter_length": 16,
          "remove_duplicated_term": true,
          "none_chinese_pinyin_tokenize": false
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "name":{
        "type": "text",
        "analyzer": "my_analyzer",
        "search_analyzer": "ik_smart"
      }
    }
  }
}

POST /test/_analyze
{
  "analyzer": "my_analyzer",
  "text": "如家酒店还不粗"
}

POST /test/_doc/1
{
  "id": 1,
  "name": "狮子"
}
POST /test/_doc/2
{
  "id": 2,
  "name": "虱子"
}

GET /test/_search
{
  "query": {
    "match": {
      "name": "掉入狮子笼咋办"
    }
  }
}

# 自动补全的索引库
PUT test2
{
  "mappings": {
    "properties": {
      "title":{
        "type": "completion"
      }
    }
  }
}

# 示例数据

POST test2/_doc
{
  "title": ["Sony", "WH-1000XM3"]
}
POST test2/_doc
{
  "title": ["SK-II", "PITERA"]
}
POST test2/_doc
{
  "title": ["Nintendo", "switch"]
}

# 自动补全查询

POST /test2/_search
{
  "suggest": {
    "title_suggest": {
      "text": "sw",
      "completion": {
        "field": "title", 
        "skip_duplicates": true, 
        "size": 10
      }
    }
  }
}

DELETE /hotel

# 酒店数据索引库

PUT /hotel
{
  "settings": {
    "analysis": {
      "analyzer": {
        "text_anlyzer": {
          "tokenizer": "ik_max_word",
          "filter": "py"
        },
        "completion_analyzer": {
          "tokenizer": "keyword",
          "filter": "py"
        }
      },
      "filter": {
        "py": {
          "type": "pinyin",
          "keep_full_pinyin": false,
          "keep_joined_full_pinyin": true,
          "keep_original": true,
          "limit_first_letter_length": 16,
          "remove_duplicated_term": true,
          "none_chinese_pinyin_tokenize": false
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "id":{
        "type": "keyword"
      },
      "name":{
        "type": "text",
        "analyzer": "text_anlyzer",
        "search_analyzer": "ik_smart",
        "copy_to": "all"
      },
      "address":{
        "type": "keyword",
        "index": false
      },
      "price":{
        "type": "integer"
      },
      "score":{
        "type": "integer"
      },
      "brand":{
        "type": "keyword",
        "copy_to": "all"
      },
      "city":{
        "type": "keyword"
      },
      "starName":{
        "type": "keyword"
      },
      "business":{
        "type": "keyword",
        "copy_to": "all"
      },
      "location":{
        "type": "geo_point"
      },
      "pic":{
        "type": "keyword",
        "index": false
      },
      "all":{
        "type": "text",
        "analyzer": "text_anlyzer",
        "search_analyzer": "ik_smart"
      },
      "suggestion":{
          "type": "completion",
          "analyzer": "completion_analyzer"
      }
    }
  }
}

GET /hotel/_search

GET /hotel/_search
{
  "suggest": {
    "suggestions": {
      "text": "sd",
      "completion": {
        "field": "suggestion",
        "skip_duplicates":true,
        "size":10
      }
    }
  }
}

```
