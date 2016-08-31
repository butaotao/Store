package com.dachen.dgroupdoctorcompany.js;

/**
 * @项目名 MedicineProject
 * @Author: zxy on 16/8/27上午10:10.
 * @描述 TODO
 */
public class MenuButtonBean {
    public String showMenu;
    public String menuType;
    /**
     * type : 1
     * menuText : 发布
     */

    public SingleMenuBean singleMenu;

    public static class SingleMenuBean {
        public String type;
        public String menuText;

        @Override
        public String toString() {
            return "SingleMenuBean{" +
                    "type='" + type + '\'' +
                    ", menuText='" + menuText + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "MenuButtonBean{" +
                "showMenu='" + showMenu + '\'' +
                ", menuType='" + menuType + '\'' +
                ", singleMenu=" + singleMenu +
                '}';
    }
}
