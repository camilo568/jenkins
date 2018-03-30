/*************************************************************
 * Test made by John Bryan Yepez Herrera
 *
 * based on:
 * https://mercurio.psl.com.co/jira/browse/EP-326
 * 
 * 
 * Test explanation usin Gherkin:
 * 
 * GIVEN the api have already topics to open
 * WHEN I send a patch request to open a specific topic
 * THEN The topic must be opened correctly
 * AND must be stored in the database as an open topic.
 * 
 * GIVEN the api have topics not closed
 * WHEN I send a patch request to close a specific topic
 * THEN The topic must be closed correctly
 * AND must be stored in the database as a closed topic.
 * 
 *************************************************************/


var rest = require('restler');
var options = { headers: { "Content-Type": 'application/json' } };

let apiGet = function (status) {
    var defer = protractor.promise.defer();
    rest.get("https://integrador2018.herokuapp.com/topics/findByStatus?status=" + status, options).on('complete', function (result) {
        if (result instanceof Error) {
            //console.log('Error:', result.message);
            defer.reject({ error: result.Error, message: result.message });
        } else {
            //console.log('Success: ', result);
            defer.fulfill(result);
        }
    });
    return defer.promise;
}

let apiPatch = function (object) {
    var defer = protractor.promise.defer();
    rest.patchJson('https://integrador2018.herokuapp.com/topics', object, options).on('complete', function (result) {
        if (result instanceof Error) {
            //console.log('Error:', result.message);
            defer.reject({ error: result.Error, message: result.message });
        } else {
            //console.log('Success: ', result);
            defer.fulfill(result);
        }
    })
    return defer.promise;
}

let isContained = function(topic, array){
    var contained = false;
    for (i = 0; i < array.length; ++i) {
        if (array[i].id == topic.id) {
            contained = true;
            break;
        }
    }
    return contained;
}

describe('Web Api', function () {

    var maxWaitTime = 60000; //miliseconds

    it("must be able to open a specified topic", () => {

        console.log("\n\nGIVEN the api have already topics to open");
        browser.wait(apiGet(0), maxWaitTime).then(topicsToOpenArray => {
            expect(topicsToOpenArray.length > 0).toEqual(true);

            console.log("WHEN I send a patch request to open a specific topic");
            var topic = topicsToOpenArray[0];
            topic.status = 1;
            browser.wait(apiPatch(topic), maxWaitTime).then(openTopic => {

                console.log("THEN The topic must be opened correctly");
                expect(openTopic.status).toEqual(1);
                
                console.log("AND must be stored in the database as an open topic.");
                browser.wait(apiGet(1), maxWaitTime).then(openTopicsArray => {
                    expect(isContained(openTopic, openTopicsArray)).toEqual(true);
                });
            });
        });
    });

    it("must be able to close a specified topic", () => {
        
        console.log("\n\nGIVEN the api have topics not closed");
        browser.wait(apiGet(1), maxWaitTime).then(notClosedTopicsArray => {
            expect(notClosedTopicsArray.length > 0).toEqual(true);

            console.log("WHEN I send a patch request to close a specific topic");
            var topic = notClosedTopicsArray[0];
            topic.status = 2;
            browser.wait(apiPatch(topic), maxWaitTime).then(closedTopic => {

                console.log("THEN The topic must be closed correctly");
                expect(closedTopic.status).toEqual(2);
                
                console.log("AND must be stored in the database as a closed topic.");
                browser.wait(apiGet(2), maxWaitTime).then(closedTopicsArray => {
                    expect(isContained(closedTopic, closedTopicsArray)).toEqual(true);
                });
            });
        });
    });
});