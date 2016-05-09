//
//  MapViewController.swift
//  BusStop
//
//  Created by Harry Liu on 2016-04-25.
//  Copyright © 2016 HarryLiu. All rights reserved.
//

import UIKit
import MapKit
import BusKit

class MapViewController: UIViewController, MKMapViewDelegate {
    
    @IBOutlet weak var mapView: MKMapView!
    var stops: [Stop] = []
    var annotations: [MKAnnotation] = []
    var selectedStop: Stop?

    override func viewDidLoad() {
        super.viewDidLoad()

        initAnnotations()
        addAnnotations()
        mapView.showAnnotations(annotations, animated: true)
        title = "Stops"
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    func initJSON() -> [AnyObject] {
        let url = NSBundle.mainBundle().URLForResource("latlong", withExtension: "json")
        let data = NSData(contentsOfURL: url!)
        
        do {
            let object = try NSJSONSerialization.JSONObjectWithData(data!, options: .AllowFragments)
            if let dictionary = object as? [AnyObject] {
                return dictionary
            } else {
                print("initJSON failed to parse JSON")
            }
        } catch {
            print("Handle error")
        }
        
        return []
    }
    
    func initAnnotations() {
        let data = initJSON()
        for stop in stops {
            let stopNumber = stop.stopNumber
            if let stopInfo = findStop(Int(stopNumber)!, data: data) {
                let coordinate = CLLocationCoordinate2D(latitude: stopInfo["lat"]!.doubleValue, longitude: stopInfo["long"]!.doubleValue)
                let lc = Location(title: stop.stopName, coordinate: coordinate, info: "", number: stop.stopNumber, stop:stop)
                annotations.append(lc)
            }
        }
    }
    
    func addAnnotations() {
        mapView.addAnnotations(annotations)
    }
    
    func findStop(number: Int, data: [AnyObject]) -> [String: AnyObject]? {
        var high = data.count-1
        var low = 0
        while high >= low {
            let middle = (low + high) / 2
            let element = data[middle] as! [String: AnyObject]
            if number > element["stop"] as! Int {
                low = middle + 1
            }
            else if number < element["stop"] as! Int {
                high = middle - 1
            }
            else {
                return data[middle] as? [String : AnyObject]
            }
        }
        return nil
    }
    
    func mapView(mapView: MKMapView, viewForAnnotation annotation: MKAnnotation) -> MKAnnotationView? {
        let identifier = "Location"

        if annotation.isKindOfClass(Location.self) {
            var annotationView = mapView.dequeueReusableAnnotationViewWithIdentifier(identifier)
            
            if annotationView == nil {
                annotationView = MKPinAnnotationView(annotation: annotation, reuseIdentifier: identifier)
                annotationView!.canShowCallout = true
                let btn = UIButton(type: .ContactAdd)
                annotationView!.rightCalloutAccessoryView = btn
            } else {
                annotationView!.annotation = annotation
            }
            return annotationView
        }
        return nil
    }
    
    func mapView(mapView: MKMapView, annotationView view: MKAnnotationView, calloutAccessoryControlTapped control: UIControl) {
        selectedStop = (view.annotation as! Location).stop
        performSegueWithIdentifier("FinishSegue", sender: self)
    }
    
    /*
    func checkForOrdering(data: [AnyObject]) {
        var num = 0
        for element in data {
            let stop = element["stop"] as! NSInteger
            if num > stop {
                print("\(num) is greater than \(stop)\n")
            }
            num = stop
        }
        print("GUCCI FAM")
    }*/

    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if selectedStop != nil {
            let vc = segue.destinationViewController as! FinishViewController
            vc.stop = selectedStop
        }
        else {
            print("didn't select a stop")
        }
    }
    

}
