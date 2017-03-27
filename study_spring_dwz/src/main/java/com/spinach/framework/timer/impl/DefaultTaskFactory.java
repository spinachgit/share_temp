package com.spinach.framework.timer.impl;

import java.util.Collection;
import java.util.Date;

import com.spinach.framework.timer.TaskEngine;
import com.spinach.framework.timer.TaskFactory;
import com.spinach.framework.timer.TaskParse;
import com.spinach.framework.timer.TaskUnit;

public class DefaultTaskFactory extends TaskFactory {

	private TaskParse parser = null;

	@Override
	public Runnable getTask(String name) {
		if (parser == null)
			return null;
		TaskUnit taskUnit = this.parser.getTaskUnit(name);
		if (taskUnit != null) {
			return taskUnit.getTask();
		}
		return null;
	}

	@Override
	public void initTasks(String filePath) {
		if (filePath == null) {
			return;
		}
		this.parser = new TaskParseImpl(filePath);
	}

	@Override
	public void startAllTasks() {
		if (this.parser == null)
			return;
		startTasks(this.parser.getTaskUnits());
	}

	@Override
	public void startTasks(Collection<TaskUnit> units) {
		if (units == null || units.isEmpty())
			return;
		for (TaskUnit taskUnit : units) {
			if (taskUnit.isRunnable()) {
				Runnable task = taskUnit.getTask();
				Date startTime = taskUnit.getStartTime();
				int priority = taskUnit.getPriority();
				long period = taskUnit.getPeriod();

				if (period == 0) {
					TaskEngine.scheduleTask(task, startTime, priority);
				} else {
					TaskEngine.scheduleTask(task, startTime, period, priority);
				}
			}
		}
	}

	@Override
	public TaskParse getTaskParse() {
		return this.parser;
	}

}
