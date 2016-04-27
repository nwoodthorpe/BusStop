//
//  Route.swift
//  BusStop
//
//  Created by Harry Liu on 2016-04-26.
//  Copyright © 2016 HarryLiu. All rights reserved.
//

struct Stop {
    
    var Number: Int
    var Name: String
    var Time: Int
    
    init(number: Int, name : String, time: Int) {
        Number = number
        Name = name
        Time = time
    }
}
