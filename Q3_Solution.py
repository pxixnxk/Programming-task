import csv
from requests_html import HTMLSession
import requests
import json

# store properties and the corresponding ID
propertyIdMap = [
    ('Type', '#type-val'),
    ('Status', '#status-val'),
    ('Priority', '#priority-val'),
    ('Resolution', '#resolution-val'),
    ('Affect Version/s', '#versions-val'),
    ('Fix Version/s', '#fixfor-val'),
    ('Components/s', '#components-val'),
    ('Assignee', '#assignee-val'),
    ('Reporter', '#reporter-val'),
    ('Votes', '#vote-data'),
    ('Watchers', '#watcher-data'),
    ('Created', '#created-val'),
    ('Updated', '#updated-val'),
    ('Resolved', '#resolutiondate-val'),
    ('Description', '#description-val'),
]

outputFile = 'properties.csv'

# Using HTMLSession to get the details
def crawl(issuekey):
    url = "https://issues.apache.org/jira/browse/CAMEL-" + issuekey
    session = HTMLSession()
    r = session.get(url)

    # Convert from JSON to Python dictionary and get comments
    url_com = "https://issues.apache.org/jira/rest/api/2/issue/CAMEL-"+issuekey+"/comment"
    resp = requests.get(url_com)
    com_json = json.loads(resp.text)
    comments = ""
    for i in range(0, com_json["total"]):  # num of comments
        comments += com_json["comments"][i]["updateAuthor"]["displayName"] + ":" + com_json["comments"][i]["created"] + ":" + com_json["comments"][i]["body"]

    # Select only elements containing certain properties and write to .csv file
    with open(outputFile, 'a') as csvFile:
        writer = csv.writer(csvFile)
        vals = []
        for name, elementId in propertyIdMap:
            value = r.html.find(elementId)
            if (not value):
                print(name, "is not found.")
                vals.append("N/A")
                continue
            vals.append(value[0].text)
        if(comments == ""):
            vals.append("N/A")
        vals.append(comments)
        writer.writerow(vals)

# create the .csv file and input the headings
def createCsv():
    with open(outputFile, 'w', newline='') as csvfile:
        fieldnames = []
        for name in propertyIdMap:
            fieldnames.append(name[0])
        fieldnames.append("Comments")
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
        writer.writeheader()

# input the key value(a number) of the issue to get the report
if __name__ == "__main__":
    issuekey = input("Please input the issue key:")
    createCsv()
    crawl(issuekey)
