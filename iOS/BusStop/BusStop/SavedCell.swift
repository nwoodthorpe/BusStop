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
    @IBOutlet weak var Nickname: UILabel!
    @IBOutlet weak var Time: UILabel!
    @IBOutlet weak var Switch: UISwitch!
    @IBAction func SwitchChanged(sender: AnyObject) {
        parent.setSwitch(routeName!, stop: stopNumber!, on: Switch.on)
        parent.save()
    }
    @IBOutlet weak var parent: HomeViewController!
    
    var routeName: String?
    var stopNumber: String?
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        initCircle()
    }
    
    func initCircle() {
        RouteNumber.layer.borderWidth = 3
        RouteNumber.layer.cornerRadius = RouteNumber.frame.width / 2
    }
    
    func initValues(number: String, nickname: String, routeName: String, stopNumber: String, time: Int, on: Bool) {
        selectionStyle = UITableViewCellSelectionStyle.None
        RouteNumber.text = String(number)
        Nickname.text = nickname
        Time.text = "\(time / 60) minutes"
        Switch.setOn(on, animated: true)
        self.routeName = routeName
        self.stopNumber = stopNumber
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
