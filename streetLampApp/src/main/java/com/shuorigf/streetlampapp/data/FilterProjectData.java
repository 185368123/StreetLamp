package com.shuorigf.streetlampapp.data;

import java.util.List;

public class FilterProjectData {

    /**
     * status : 0000
     * msg : 操作成功
     * data : {"projects":[{"id":"20882","name":"办公室LoRa测试","network":"3","tag":"B"},{"id":"21172","name":"办公室NBIot测试","network":"1","tag":"B"}]}
     */

    private String status;
    private String msg;
    private DataBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<ProjectsBean> projects;

        public List<ProjectsBean> getProjects() {
            return projects;
        }

        public void setProjects(List<ProjectsBean> projects) {
            this.projects = projects;
        }

        public static class ProjectsBean {
            /**
             * id : 20882
             * name : 办公室LoRa测试
             * network : 3
             * tag : B
             */

            private String id;
            private String name;
            private String network;
            private String tag;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNetwork() {
                return network;
            }

            public void setNetwork(String network) {
                this.network = network;
            }

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }

            @Override
            public String toString() {
                return "ProjectsBean{" +
                        "id='" + id + '\'' +
                        ", name='" + name + '\'' +
                        ", network='" + network + '\'' +
                        ", tag='" + tag + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "projects=" + projects +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "FilterProjectData{" +
                "status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
