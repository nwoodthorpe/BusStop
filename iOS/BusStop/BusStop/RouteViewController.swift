//
//  RouteViewController.swift
//  BusStop
//
//  Created by Harry Liu on 2016-04-25.
//  Copyright © 2016 HarryLiu. All rights reserved.
//

import UIKit

class RouteViewController: UITableViewController {
    
    var routes: [Route] = []
    var currentRoute: Route? = nil

    override func viewDidLoad() {
        super.viewDidLoad()
        
        initJSON()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
    }
    
    func initJSON() {
        let url = NSBundle.mainBundle().URLForResource("stops", withExtension: "json")
        let data = NSData(contentsOfURL: url!)
        
        do {
            let object = try NSJSONSerialization.JSONObjectWithData(data!, options: .AllowFragments)
            if let dictionary = object as? [AnyObject] {
                readJSONObject(dictionary)
            } else {
                print("initJSON failed to parse JSON")
            }
        } catch {
            print("Handle error")
        }
    }
    
    func readJSONObject(object: [AnyObject]) {
        for item in object {
            let info = item["RouteDirection"] as! [String: AnyObject]
            let route = Route(number: (info["PublicIdentifier"] as! NSString).integerValue, name: info["Description"] as! String, text: item["Text"] as! String, direction: info["Direction"] as! String, directionName: info["DirectionName"] as! String, stops: item["Stops"] as! [String])
            routes.append(route)
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source

    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }

    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return routes.count
    }

    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("RouteCell", forIndexPath: indexPath) as! RouteCell
        let route = routes[indexPath.row]
        cell.RouteName.text = "\(route.Name) - \(route.DirectionName)"
        cell.RouteNumber.text = String(route.Number)

        return cell
    }
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        //super.tableView(tableView, didSelectRowAtIndexPath: indexPath)
        
        currentRoute = routes[indexPath.row]
        performSegueWithIdentifier("MapSegue", sender: self)
    }

    /*
    // Override to support conditional editing of the table view.
    override func tableView(tableView: UITableView, canEditRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    */

    /*
    // Override to support editing the table view.
    override func tableView(tableView: UITableView, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath) {
        if editingStyle == .Delete {
            // Delete the row from the data source
            tableView.deleteRowsAtIndexPaths([indexPath], withRowAnimation: .Fade)
        } else if editingStyle == .Insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }    
    }
    */

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
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
        
        if segue.identifier == "MapSegue" {
            if let vc = segue.destinationViewController as? MapViewController {
                let stoptext = currentRoute!.Stops
                for text in stoptext {
                    let num = Int(text.characters.split{$0 == " "}.map(String.init)[0])!
                    let newstop = Stop(number: num, name: text, line: currentRoute!.Number, time: 0)
                    vc.stops.append(newstop)
                }
            }
        }
        
    }
    

}
