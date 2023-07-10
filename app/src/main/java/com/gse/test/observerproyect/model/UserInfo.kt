package com.gse.test.observerproyect.model

class UserInfo(
    var documentType: String,
    var documentNumber: String,
    var firstName: String,
    var secondName: String,
    var surname: String,
    var secondSurname: String,
    var email: String,
    var address: String,
    var department: String,
    var location: String,
    var phone: String,
    var expeditionDate: String
) {

    override fun toString(): String {
        return "UserInfo{" +
                "documentType='" + documentType + '\'' +
                ", documentNumber='" + documentNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", surname='" + surname + '\'' +
                ", secondSurname='" + secondSurname + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", department='" + department + '\'' +
                ", location='" + location + '\'' +
                ", phone='" + phone + '\'' +
                ", expeditionDate='" + expeditionDate + '\'' +
                '}'
    }
}