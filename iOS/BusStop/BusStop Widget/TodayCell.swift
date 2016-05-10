//
//  TodayCell.swift
//  BusStop
//
//  Created by Harry Liu on 2016-05-08.
//  Copyright © 2016 HarryLiu. All rights reserved.
//

import UIKit

class TodayCell: UITableViewCell {

    @IBOutlet weak var RouteNumber: UILabel!
    @IBOutlet weak var Nickname: UILabel!
    @IBOutlet weak var Time: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        initCircle()
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func initCircle() {
        RouteNumber.layer.borderColor = UIColor.whiteColor().CGColor
        RouteNumber.layer.borderWidth = 2
        RouteNumber.layer.cornerRadius = RouteNumber.frame.width / 2
    }

}
