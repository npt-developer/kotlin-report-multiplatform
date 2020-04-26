//
//  Person.swift
//  iosApp
//
//  Created by Trần Hữu Hiền Hiền on 4/25/20.
//

import Foundation

class Person
{
    
    var name: String = ""
    var age: Int = 0
    var id: Int = 0
    
    init(id:Int, name:String, age:Int)
    {
        self.id = id
        self.name = name
        self.age = age
    }
    
}
