//
//  Route.swift
//  BusStop
//
//  Created by Harry Liu on 2016-04-26.
//  Copyright © 2016 HarryLiu. All rights reserved.
//

public class Route {
    
    public var Number: String
    public var Name: String
    public var Text: String
    public var Direction: String
    public var DirectionName: String
    public var Stops: [String]
    
    public init(number: String, name: String, text: String, direction: String, directionName: String, stops: [String]) {
        Number = number
        Name = name
        Text = text
        Direction = direction
        DirectionName = directionName
        Stops = stops
    }
    
}