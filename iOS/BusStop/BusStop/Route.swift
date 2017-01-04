//
//  Route.swift
//  BusStop
//
//  Created by Harry Liu on 2016-04-26.
//  Copyright © 2016 HarryLiu. All rights reserved.
//

open class Route {
    
    open var Number: String
    open var Name: String
    open var Text: String
    open var Direction: String
    open var DirectionName: String
    open var Stops: [String]
    
    public init(number: String, name: String, text: String, direction: String, directionName: String, stops: [String]) {
        Number = number
        Name = name
        Text = text
        Direction = direction
        DirectionName = directionName
        Stops = stops
    }
    
}
