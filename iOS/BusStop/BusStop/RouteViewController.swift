//
//  RouteViewController.swift
//  BusStop
//
//  Created by Harry Liu on 2016-04-25.
//  Copyright © 2016 HarryLiu. All rights reserved.
//

import UIKit
import BusKit

class RouteViewController: UITableViewController {
    
    var routes: [Route] = []
    var currentRoute: Route?

    override func viewDidLoad() {
        super.viewDidLoad()
        
        initJSON()
        title = "Routes"
    }
    
    func initJSON() {
        let url = Bundle.main.url(forResource: "stops", withExtension: "json")
        let data = try? Data(contentsOf: url!)
        
        do {
            let object = try JSONSerialization.jsonObject(with: data!, options: .allowFragments)
            if let dictionary = object as? [AnyObject] {
                readJSONObject(dictionary)
            } else {
                print("initJSON failed to parse JSON")
            }
        } catch {
            print("Handle error")
        }
    }
    
    func readJSONObject(_ object: [AnyObject]) {
        for item in object {
            let info = item["RouteDirection"] as! [String: AnyObject]
            var number = info["PublicIdentifier"] as! String
            let name = info["Description"] as! String
            let text = item["Text"] as! String
            let direction = info["Direction"] as! String
            let directionName = info["DirectionName"] as! String
            let stops = item["Stops"] as! [String]
            if number == "7" {
                number = text.characters.split{$0 == " "}.map(String.init)[0]
            }
            let route = Route(number: number, name: name, text: text, direction: direction, directionName: directionName, stops: stops)
            routes.append(route)
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return routes.count
    }

    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "RouteCell", for: indexPath) as! RouteCell
        let route = routes[indexPath.row]
        cell.RouteName.text = "\(route.Name) - \(route.DirectionName)"
        cell.RouteNumber.text = String(route.Number)

        return cell
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        //super.tableView(tableView, didSelectRowAtIndexPath: indexPath)
        
        currentRoute = routes[indexPath.row]
        performSegue(withIdentifier: "MapSegue", sender: self)
    }

    /*
    // Override to support rearranging the table view.
    override func tableView(tableView: UITableView, moveRowAtIndexPath fromIndexPath: NSIndexPath, toIndexPath: NSIndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(tableView: UITableView, canMoveRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    */

    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "MapSegue" {
            if let vc = segue.destination as? MapViewController {
                let stoptext = currentRoute!.Stops
                for text in stoptext {
                    let num = text.characters.split{$0 == " "}.map(String.init)[0]
                    let newstop = Stop(stopNumber: num, stopName: text, routeNumber: currentRoute!.Number, routeName: currentRoute!.Name)
                    vc.stops.append(newstop)
                }
            }
        }
        
    }
    

}
