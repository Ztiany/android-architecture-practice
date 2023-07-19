package com.app.base.widget.address;

import java.util.List;

interface IName {

    AddressToken getAddressToken();

    List<IName> getChildren();

    class AddressToken {

        static final int COUNTRY = 1;
        static final int PROVINCE = 2;
        static final int CITY = 3;
        static final int AREA = 4;

        private int identifying;
        private String name;

        int getIdentifying() {
            return identifying;
        }

        public void setIdentifying(int identifying) {
            this.identifying = identifying;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}