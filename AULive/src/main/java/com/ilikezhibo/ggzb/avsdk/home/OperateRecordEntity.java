package com.ilikezhibo.ggzb.avsdk.home;

import java.util.List;

/**
 * Created by stephensun on 2017/2/14.
 */
public class OperateRecordEntity {

    /**
     * data : [{"content":"修改主播类型为:签约主播","created":"1487051137","id":"105","operator":"admin","type":"upinfo","uid":"10403319"},{"content":"修改用户类型为:1","created":"1487051042","id":"104","operator":"admin","type":"upinfo","uid":"10403319"},{"content":"解除警告主播","created":"1487047243","id":"103","type":"cancelwarning","uid":"10403319"},{"content":"警告主播","created":"1487047240","id":"102","type":"detectwarning","uid":"10403319"},{"content":"解除警告主播","created":"1487047232","id":"101","type":"cancelwarning","uid":"10403319"},{"content":"onetone:0","created":"1487035370","id":"99","operator":"admin","type":"upinfo","uid":"10403319"},{"content":"onetone:1","created":"1486986624","id":"98","operator":"admin","type":"upinfo","uid":"10403319"},{"content":"onetone:1","created":"1486986404","id":"97","operator":"admin","type":"upinfo","uid":"10403319"},{"content":"onetone:1","created":"1486986375","id":"96","operator":"admin","type":"upinfo","uid":"10403319"},{"content":"修改直播状态为:在直播","created":"1486977109","id":"95","operator":"admin","type":"updatelive","uid":"10403319"},{"content":"修改直播状态为:在直播","created":"1486977080","id":"94","operator":"admin","type":"updatelive","uid":"10403319"},{"content":"status:0","created":"1486976902","id":"93","operator":"admin","type":"upinfo","uid":"10403319"},{"content":"status:1","created":"1486976891","id":"92","operator":"admin","type":"upinfo","uid":"10403319"},{"content":"修改直播状态为:未直播","created":"1486784731","id":"90","operator":"金翼","type":"updatelive","uid":"10403319"},{"content":"status:0","created":"1486784710","id":"89","operator":"金翼","type":"upinfo","uid":"10403319"},{"content":"修改直播状态为:未直播","created":"1486711282","id":"88","operator":"admin","type":"updatelive","uid":"10403319"},{"content":"修改直播状态为:未直播","created":"1486710852","id":"87","operator":"admin","type":"updatelive","uid":"10403319"},{"content":"修改直播状态为:未直播","created":"1486710618","id":"86","operator":"admin","type":"updatelive","uid":"10403319"},{"content":"type:0","created":"1486710615","id":"85","operator":"admin","type":"upinfo","uid":"10403319"}]
     * msg :
     * stat : 200
     */

    private String msg;
    private int stat;
    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStat() {
        return stat;
    }

    public void setStat(int stat) {
        this.stat = stat;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * content : 修改主播类型为:签约主播
         * created : 1487051137
         * id : 105
         * operator : admin
         * type : upinfo
         * uid : 10403319
         */

        private String content;
        private String created;
        private String id;
        private String operator;
        private String type;
        private String uid;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }
    }
}
