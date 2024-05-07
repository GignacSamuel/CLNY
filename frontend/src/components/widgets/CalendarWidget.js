import React from "react";
import { Calendar } from "../ui/calendar"

function CalendarWidget() {
    return (
        <div className="flex justify-center items-center">
            <Calendar
                mode="single"
                selected={new Date()}
                className="rounded-md border"
            />
        </div>
    )
}

export default CalendarWidget;
