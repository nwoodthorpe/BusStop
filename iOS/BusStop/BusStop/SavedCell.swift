//
//  SavedCell.swift
//  BusStop
//
//  Created by Harry Liu on 2016-04-25.
//  Copyright © 2016 HarryLiu. All rights reserved.
//

import UIKit

class SavedCell: UITableViewCell {

    @IBOutlet weak var RouteNumber: UILabel!
    @IBOutlet weak var RouteName: UILabel!
    @IBOutlet weak var Time: UILabel!
    @IBOutlet weak var Switch: UISwitch!
    @IBAction func SwitchChanged(sender: AnyObject) {
        
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        initCircle()
    }
    
    func initCircle() {
        RouteNumber.layer.borderWidth = 3
        RouteNumber.layer.cornerRadius = RouteNumber.frame.width / 2
    }
    
    func initValues(number number: Int, name: String, time: Int) {
        RouteNumber.text = String(number)
        RouteName.text = name
        Time.text = "\(time) minutes"
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
