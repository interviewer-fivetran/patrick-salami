# Syncing News
__Please read through this README in its entirety before starting the problem.__

## Table of Contents
- [Objective](#objective)
  - [What is a data pipeline?](#what-is-a-data-pipeline)
- [Build a Connector](#build-a-connector)
  - [Task 0: Complete the schema](#task-0-complete-the-schema)
  - [Task 1: Implement Connector Logic](#task-1-implement-connector-logic)
- [Logistics](#logistics)
  - [Running the spec](#running-the-spec)
  - [Submitting](#submitting)

## Objective
The objective is to complete the implementation of a data pipeline.

### What is a data pipeline?
A data pipeline funnels data from source to destination. 

A central problem that Fivetran tackles is
- extracting all of the information from a source
- restructuring this information as tables in a well-designed schema, and
- writing it into a destination data warehouse.

Analysts then query the data warehouse using SQL.

We can think of a data pipeline as having 2 main components that connect source and destination:

1. The __Connector__ handles source-side data and is responsible for inserting records into the pipeline. There is one Connector per supported source.

2. The __Core__ receives data from _multiple connectors_ and is expected to process that data in a efficient, consistent, and generalizable manner before sending it to the warehouse.


_Source_  → __[ Connector ]__ → __[ Core ]__ → _Destination (Warehouse)_


## Build a Connector

### Task 0: Complete the schema
#### Description
The `Newspaper` API (quite the misnomer given that it's an online platform) provides metadata about articles and their read counts. 

Fivetran's goal is to capture all of the data the API has to offer and conveniently provide it for analysts. 

We want to design a schema. We also want analysts to be able to `JOIN` the `Stories` and `StoryCounts` tables.

#### Mission
Implement `Schema#STORIES` and `Schema#STORY_COUNTS`. 

#### Tips
- It's recommended to query the API. You may add a new method to `Spec.java` with a `@Test` annotation, in which you could query the API to get a sense of its behavior. 
- Alternatively, you may refer to the definitions of `NewspaperResponse` and `NewspaperStory` to get a picture of API response shape.

### Task 1: Implement Connector Logic
#### Description
Now that we have a sensible schema, it's time to populate it!

#### Mission
Implement `NewspaperUpdater#update`. 

This method is called once per sync. Syncs are run at intervals (not continuously). The goal of each call to `NewspaperUpdater#update` is to extract as much data from the source as possible, and to persist some sort of state for the next run.

#### Tips
- Refer to the JavaDoc for descriptions of method arguments.
- Remember that, at a high level, the `Updater` is where:
  - the API should be queried and the data inserted into the Core pipeline.
  - progress should be recorded.

#### Test
At this point, `Spec#testStories`, `Spec#testStoryCounts`, and `Spec#testState` should all pass.

## Logistics
### Running the spec
You are encouraged to run the provided tests in `Spec.java`.

You're also welcome to create any custom tests, or mock methods to prod at the API.

__Note:__ Keep in mind that the provided tests are __not__ comprehensive. Not all of the intended or preferred behavior of your program is tested in `Spec`. Your submitted solution will be evaluated and discussed further by reviewers.

### Submitting
Simply push your solution to the master branch. Your most recent commit will be evaluated.

