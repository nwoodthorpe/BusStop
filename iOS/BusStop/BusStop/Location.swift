//
//  Location.swift
//  BusStop
//
//  Created by Harry Liu on 2016-04-28.
//  Copyright © 2016 HarryLiu. All rights reserved.
//

import UIKit
import MapKit

class Location: NSObject, MKAnnotation {

    var title: String?
    var coordinate: CLLocationCoordinate2D
    var info: String
    var number: Int
    
    init(title: String, coordinate: CLLocationCoordinate2D, info: String, number: Int) {
        self.title = title
        self.coordinate = coordinate
        self.info = info
        self.number = number
    }
}
